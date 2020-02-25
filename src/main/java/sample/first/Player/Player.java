package sample.first.Player;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.ui.ProgressBar;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import static sample.first.Main.end;

public class Player extends Component {

    private int dx = 0;
    private int dy = 0;
    private AnimatedTexture texture;
    private AnimationChannel animIdle, animWalkDown, animWalkLeft, animWalkUp;
    private int attack;
    private int defence;
    private int speed;
    private ProgressBar health;
    private int potions;
    private Text[] labels;

    public Player() {
        animIdle = new AnimationChannel(FXGL.image("princessIdle.png"),
                3, 20, 32, Duration.millis(1000), 0, 1);
        animWalkDown = new AnimationChannel(FXGL.image("princess.png"),
                20, 16, 32, Duration.millis(500), 0, 2);
        animWalkLeft = new AnimationChannel(FXGL.image("princessLeft.png"),
                3, 18, 28, Duration.millis(500), 0, 2);
        animWalkUp = new AnimationChannel(FXGL.image("princess.png"),
                20, 16, 32, Duration.millis(500), 6, 8);
        texture = new AnimatedTexture(animIdle);
        this.attack = 10;
        this.defence = 4;
        this.speed = 8000;
        this.potions = 3;
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(10, 10));
        entity.getViewComponent().addChild(texture);
        PlayerHealthBar newbar = new PlayerHealthBar(); //On instancie une nouvelle barre de vie
        this.health = newbar.getBar();

        String style = "-fx-font-size: 2em;-fx-stroke: black;-fx-text-fill: white;";
        Integer[] stats = new Integer[]{this.attack, this.defence, this.speed / 1000, this.potions};
        labels = new Text[4];
        int index = 0;
        for (int i = 945; i < 1300; i += 96) { //Boucle qui permet d'afficher et d'enregistrer les stats du player
            Text attack = new Text(Integer.toString(stats[index]));
            attack.setTranslateX(i);
            attack.setTranslateY(635);
            attack.setStyle(style);
            FXGL.getGameScene().addUINode(attack);
            labels[index] = attack;
            index++;
        }

    }

    @Override
    public void onUpdate(double tpf) {
        AnimationChannel act_texture = texture.getAnimationChannel();
        if (dx != 0) {
            if (dx < -1 && (act_texture != animWalkLeft)) {
                texture.loopAnimationChannel(animWalkLeft);
            } else if (dx > 1 && (act_texture != animWalkLeft)) {
                texture.loopAnimationChannel(animWalkLeft);
            }
        } else if (dy != 0) {
            if (dy < -1 && (act_texture != animWalkUp)) {
                texture.loopAnimationChannel(animWalkUp);
            } else if (dy > 1 && (act_texture != animWalkDown)) {
                texture.loopAnimationChannel(animWalkDown);
            }
        }
        entity.getComponent(PhysicsComponent.class).setLinearVelocity(dx * tpf, dy * tpf);
        dx *= 0.5;
        dy *= 0.5;
        if (Math.abs(dx) <= 1)
            dx = 0;
        if (Math.abs(dy) <= 1)
            dy = 0;
        if (dx == 0 && dy == 0 && act_texture != animIdle)
            texture.loopAnimationChannel(animIdle);
    }

    public void moveRight() {
        dx = this.speed;
        dy = 0;
        getEntity().setScaleX(-1);
    }

    public void moveLeft() {

        dx = -this.speed;
        dy = 0;
        getEntity().setScaleX(1);

    }

    public void moveUp() {

        dy = -this.speed;
        dx = 0;
        getEntity().setScaleY(1);

    }

    public void moveDown() {

        dy = this.speed;
        dx = 0;
        getEntity().setScaleY(1);
    }

    public void reloadBar(double increment) {
        ProgressBar bar = this.health;
        double value = bar.getCurrentValue();
        value += increment;
        bar.setCurrentValue(value);
        if (value <= 0) {
            end(false);
        } else if (value <= 30) {
            bar.setFill(Color.RED);
            FXGL.getAudioPlayer().playSound(FXGL.getAssetLoader().loadSound("nolife.mp3"));
        } else
            bar.setFill(Color.GREEN);
    }

    public void getDamage(int damage) {
        double increment = this.defence <= 0 ? damage +  this.defence : damage - this.defence;
        this.reloadBar(increment);
    }

    public void heal(int heal) {
        if (this.potions >= 1) {
            this.reloadBar(heal);
            this.potions--;
            this.labels[3].setText(Integer.toString(this.potions));
        }
    }

    public void addPotion() {
        this.potions++;
        this.labels[3].setText(Integer.toString(this.potions));
    }

    public void incrementAttack(int increment) {
        this.attack += increment;
        this.labels[0].setText(Integer.toString(this.attack));
    }

    public void incrementDefence(int increment) {
        this.defence += increment;
        this.labels[1].setText(Integer.toString(this.defence));
    }

    public void incrementSpeed(int increment) {
        this.speed += increment;
        this.labels[2].setText(Integer.toString(this.speed / 1000));
    }
}

