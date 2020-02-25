package sample.first;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.Viewport;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.cutscene.Cutscene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.ui.ProgressBar;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import sample.first.Entites.Bonus;
import sample.first.Entites.Boss;
import sample.first.Entites.Enemy;
import sample.first.Factory.EnnemyFactory;
import sample.first.Factory.MySceneFactory;
import sample.first.Factory.WorldEntityFactory;
import sample.first.Player.Player;
import sample.first.Scenes.EndScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Main extends GameApplication {
    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(1280);
        gameSettings.setHeight(700);
        gameSettings.setTitle("Leena");
        gameSettings.setExperimentalNative(true);
        gameSettings.setMenuEnabled(true);
        gameSettings.setSceneFactory(new MySceneFactory());
        gameSettings.setCloseConfirmation(false);

    }

    protected Entity player;
    protected static Music music;

    @Override
    protected void onPreInit() {
        music = FXGL.getAssetLoader().loadMusic("musicGame.mp3");
        FXGL.getAudioPlayer().loopMusic(music);
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new WorldEntityFactory());
        FXGL.getGameWorld().addEntityFactory(new EnnemyFactory());
        FXGL.getGameState().setValue("spawners", new ArrayList<Point2D>());
        FXGL.setLevelFromMap("level1.tmx");
        PhysicsComponent pc = FXGL.getGameState().getObject("pc");
        player = FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(200, 200)
                .with(new Player())
                .with(new CollidableComponent(true))
                .bbox(new HitBox(BoundingShape.box(20, 20)))
                .with(pc)
                .buildAndAttach();
        FXGL.run(new Runnable() {
            @Override
            public void run() {
                ArrayList em = (ArrayList) FXGL.getGameWorld().getEntitiesByType(EntityType.ENNEMY);
                if (FXGL.getGameState().getBoolean("intro") && FXGL.getGameState().getInt("maxEnemies") > 0 && FXGL.random(0, 10) > 2)
                    FXGL.spawn("Ennemy");
                if (FXGL.random(0, 100) >= 95)
                    FXGL.spawn("Bonus");
            }
        }, Duration.millis(500));
    }

    protected void initPhysics() {
        FXGL.getPhysicsWorld().setGravity(0f, 0f);

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler
                (EntityType.PLAYER, EntityType.ENNEMY) {
            @Override
            protected void onCollisionBegin(Entity player, Entity ennemy) {
                player.getComponent(Player.class).getDamage(-10);
                FXGL.play("objectivedone.mp3");
                ennemy.removeFromWorld();
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler
                (EntityType.ARROW, EntityType.ENNEMY) {
            @Override
            protected void onCollisionBegin(Entity arrow, Entity ennemy) {
                ennemy.getComponent(Enemy.class).getDamage(20);
                arrow.removeFromWorld();
                FXGL.getAudioPlayer().playSound(FXGL.getAssetLoader().loadSound("arrow.mp3"));
                FXGL.getGameState().increment("enemiesKilled", +1);
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler
                (EntityType.ARROW, EntityType.DECOR) {
            @Override
            protected void onCollisionBegin(Entity arrow, Entity wall) {
                arrow.removeFromWorld();
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler
                (EntityType.ARROW, EntityType.BOSS) {
            @Override
            protected void onCollisionBegin(Entity arrow, Entity boss) {
                arrow.removeFromWorld();
                boss.getComponent(Boss.class).incrementLife(-10);
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler
                (EntityType.BOSS, EntityType.PLAYER) {
            @Override
            protected void onCollision(Entity boss, Entity player) {
                player.getComponent(Player.class).getDamage(-1);
                FXGL.play("objectivedone.mp3");
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler
                (EntityType.PLAYER, EntityType.FIREBALL) {
            @Override
            protected void onCollision(Entity player, Entity fireball) {
                player.getComponent(Player.class).getDamage(-15);
                fireball.removeFromWorld();
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler
                (EntityType.PLAYER, EntityType.BONUS) {
            @Override
            protected void onCollisionBegin(Entity player, Entity bonus) {
                bonus.getComponent(Bonus.class).getBonus();
            }
        });
    }

    protected void initInput() {
        Input input = FXGL.getInput();
        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                if (FXGL.getGameState().getBoolean("intro")) {
                    reinitCamera();
                    player.getComponent(Player.class).moveRight();
                }
            }
        }, KeyCode.D);
        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                if (FXGL.getGameState().getBoolean("intro")) {
                    reinitCamera();
                    player.getComponent(Player.class).moveLeft();
                }
            }
        }, KeyCode.A);
        input.addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                if (FXGL.getGameState().getBoolean("intro")) {
                    reinitCamera();
                    player.getComponent(Player.class).moveUp();
                }
            }
        }, KeyCode.W);
        input.addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                if (FXGL.getGameState().getBoolean("intro")) {
                    reinitCamera();
                    player.getComponent(Player.class).moveDown();
                }
            }
        }, KeyCode.S);
        input.addAction(new UserAction("Attack") {
            @Override
            protected void onActionBegin() {
                if (FXGL.getGameState().getBoolean("intro")) {
                    reinitCamera();
                    spawnArrow();
                }
            }
        }, MouseButton.PRIMARY);
        input.addAction(new UserAction("Begin") {
            @Override
            protected void onActionBegin() {
                if (FXGL.getGameState().getObject("boss_Bar").getClass().equals(PhysicsComponent.class))
                    intro();
            }
        }, KeyCode.R);
        input.addAction(new UserAction("Heal") {
            @Override
            protected void onActionBegin() {
                if (FXGL.getGameState().getBoolean("intro"))
                    player.getComponent(Player.class).heal(20);
            }
        }, KeyCode.SPACE);
        input.addAction(new UserAction("Die") {
            @Override
            protected void onActionBegin() {
                player.getComponent(Player.class).reloadBar(-100);
            }
        }, KeyCode.M);
        input.addAction(new UserAction("SpawnBonus") {
            @Override
            protected void onActionBegin() {
                FXGL.spawn("Bonus");
            }
        }, KeyCode.L);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        PhysicsComponent pc = new PhysicsComponent();
        pc.setBodyType(BodyType.DYNAMIC);
        vars.put("enemiesKilled", 0);
        vars.put("pc", pc);
        vars.put("maxEnemies", 0);
        vars.put("ennemiesCanSpawn", true);
        vars.put("intro", false);
        vars.put("boss_Bar", new PhysicsComponent());
    }

    @Override
    protected void initUI() {
        Text begin = new Text("Press R to begin");
        begin.setStyle("-fx-font-size: 5em");
        begin.setStroke(Color.WHITE);
        begin.setFill(Color.BLACK);
        begin.setTranslateX(350);
        begin.setTranslateY(350);
        FXGL.getGameScene().addUINode(begin);
        FXGL.getGameState().setValue("begin_text", begin);
    }

    private void spawnArrow() {
        Entity player = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).get(0); //On récup l'entity player
        Point2D dir = new Point2D(FXGL.getInput().getMouseXUI() - player.getX(),
                FXGL.getInput().getMouseYUI() - player.getY()).normalize().multiply(9); // On créer le vecteur entre le click et le player
        double a = Math.abs(FXGL.getInput().getMouseXUI() - player.getX());
        double b = Math.abs(FXGL.getInput().getMouseYUI() - player.getY());
        double angle = Math.acos(a / ((a * a) - (b * b))); //On calcule l'angle de rotation du sprite pour qu'il vise bien le click

        angle = angle * (180.0d / Math.PI);
        FXGL.spawn("Arrow", new SpawnData(player.getX(), player.getY()).put("dir", dir).put("angle", angle).put("sprite", "Arrow.png"));
    }

    public static void end(boolean isWin) {
        FXGL.getAudioPlayer().stopMusic(music);
        FXGL.getGameController().pushSubScene(new EndScreen(isWin));
    }

    public void intro() {
        Viewport viewport = FXGL.getGameScene().getViewport(); //Permet de faire un zoom centré
        viewport.setBounds(0, 0, 200, 500);
        viewport.setZoom(2);

        List<String> lines = FXGL.getAssetLoader().loadText("cutscene0.txt"); //On load + execute le dialogue
        Cutscene cutscene = new Cutscene(lines);
        FXGL.getCutsceneService().startCutscene(cutscene);
        FXGL.set("intro", true);
        FXGL.removeUINode((Node) FXGL.getGameState().getObject("begin_text"));

        ProgressBar boss = new ProgressBar(false); // On instancie la barre d'arrivée du boss
        boss.setCurrentValue(0);
        boss.setMaxValue(400);
        boss.setFill(Color.WHITE);
        boss.setScaleX(1.5);
        boss.setScaleY(2);
        boss.setTranslateX(190);
        boss.setTranslateY(170);
        boss.setLabelVisible(false);
        FXGL.getGameState().setValue("boss_Bar", boss);
        FXGL.getGameScene().addUINode(boss);

    }

    public void reinitCamera() {
        Viewport viewport = FXGL.getGameScene().getViewport(); //On réinit la caméra
        viewport.setBounds(0, 0, 1260, 720);
        viewport.setZoom(1);

        ProgressBar boss = FXGL.getGameState().getObject("boss_Bar"); // On décale la boss bar avec la nouvelle caméra
        boss.setScaleX(0.8);
        boss.setScaleY(1.5);
        boss.setTranslateX(50);
        boss.setTranslateY(80);
        FXGL.getGameState().setValue("maxEnemies", 1);
    }

    static public void incrementBossBar(double increment) {
        ProgressBar boss = FXGL.getGameState().getObject("boss_Bar");
        double value = boss.getCurrentValue() + increment;
        boss.setCurrentValue(value);
        if (value > 200)
            boss.setFill(Color.YELLOW);
        if (value > 300)
            boss.setFill(Color.ORANGE);
        if (value >= 400 && FXGL.getGameWorld().getEntitiesByType(EntityType.BOSS).size() == 0) {
            FXGL.spawn("Boss");
            FXGL.getGameScene().removeUINode(boss);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}