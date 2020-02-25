package sample.first.Entites;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.ui.ProgressBar;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import sample.first.EntityType;
import sample.first.Main;

public class Boss extends Component {
    private AnimatedTexture texture;
    private AnimationChannel animIdle;
    private double life;
    private ProgressBar bar;
    private int rank;

    public Boss() {
        Integer[] toRan = new Integer[]{0, 3, 6, 9}; // Ces numéros représentent la position des différent sprite
        int rank = toRan[FXGL.random(0, 3)];
        animIdle = new AnimationChannel(FXGL.image("Boss.png"),
                3, 96, 96, Duration.millis(1000), rank, rank + 2);
        texture = new AnimatedTexture(animIdle);
        life = 400+ rank*20;
        bar = new ProgressBar(true);
        bar.setCurrentValue(400 + rank*20);
        bar.setMaxValue(400 + rank*20);
        bar.setFill(Color.GREEN);
        bar.setScaleX(0.5);
        bar.setScaleY(1);
        bar.setLabelVisible(false);
        FXGL.getAudioPlayer().playSound(FXGL.getAssetLoader().loadSound("boss.mp3"));
        FXGL.play("boss.mp3");
    }

    public void incrementLife(double increment) {
        this.life += increment - this.rank;
        bar.setTraceFill(Color.RED);
        bar.setCurrentValue(life);
        System.out.println("New Life : " + this.life);
        if (life < (150))
            bar.setFill(Color.YELLOW);
        if (life < (75))
            bar.setFill(Color.ORANGE);
        if (life < (25))
            bar.setFill(Color.RED);
        if (life <= 0) {
            Main.end(true);
        }
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(10, 10));
        entity.getViewComponent().addChild(texture);
        bar.setTranslateX(entity.getCenter().getX());
        bar.setTranslateY(entity.getCenter().getY() - 60);
        FXGL.getGameScene().addUINode(bar);
    }

    @Override
    public void onUpdate(double tpf) {
        Entity player = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).get(0);
        Point2D pos = player.getPosition();
        Point2D me = entity.getPosition();
        bar.setTranslateX(entity.getCenter().getX());
        bar.setTranslateY(entity.getCenter().getY() - 50);
        double x = pos.getX() - me.getX();
        double y = pos.getY() - me.getY();
        double movespeed = (5 + rank) * tpf;
        if (player.distance(entity) <= 300) {
            movespeed += ((20 + rank) * tpf);
        }
        if (FXGL.random(0, 100) > 95) {
            this.fireball(player);
        }
        entity.getComponent(PhysicsComponent.class).setLinearVelocity(x * movespeed, y * movespeed);
    }

    public void fireball(Entity player) {
        double a = Math.abs(entity.getX() - player.getX());
        double b = Math.abs(entity.getY() - player.getY());
        double angle = Math.acos(a / ((a * a) - (b * b)));
        Point2D dir = new Point2D(player.getX() - entity.getX(),
                player.getY() - entity.getY()).normalize().multiply(9);
        angle = angle * (180.0d / Math.PI);
        FXGL.spawn("Arrow", new SpawnData(entity.getX(), entity.getY()).put("dir", dir).put("angle", angle).put("sprite", "fireball.png"));
    }
}
