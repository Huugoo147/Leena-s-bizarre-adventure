package sample.first.Entites;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import sample.first.EntityType;
import sample.first.Main;

public class Enemy extends Component {
    private AnimatedTexture texture;
    private AnimationChannel animIdle, animWalkDown, animWalkLeft, animWalkUp;
    private int attack;
    private int defence;
    private int speed;
    private int life;


    public Enemy(String sprite, int rank) {
        animIdle = new AnimationChannel(FXGL.image(sprite),
                3, 32, 32, Duration.millis(1000), 0, 2);
        animWalkDown = new AnimationChannel(FXGL.image(sprite),
                3, 32, 32, Duration.millis(500), 0, 2);
        animWalkLeft = new AnimationChannel(FXGL.image(sprite),
                3, 32, 32, Duration.millis(500), 3, 5);
        animWalkUp = new AnimationChannel(FXGL.image(sprite),
                3, 32, 32, Duration.millis(500), 10, 12);
        texture = new AnimatedTexture(animIdle);
        if (rank == 1)
            this.setAtributes(8, 5, 10);
        else if (rank == 2 || rank == 3)
            this.setAtributes(5, 0, 5);
        else if (rank >= 4 && rank <= 6)
            this.setAtributes(0, 0, 10);
        else if (rank == 7 || rank == 8)
            this.setAtributes(5, 0, -10);
        else if (rank >= 9 && rank <= 12)
            this.setAtributes(-4, -3, 20);
        else if (rank == 13 || rank == 14)
            this.setAtributes(10, 3, -15);
        else if (rank == 15)
            this.setAtributes(10, -3, 10);
        else
            this.setAtributes(-4, -3, 20);
        this.life = 20 + FXGL.random(0, 10);
    }

    public void setAtributes(int bonus_attack, int bonus_defence, int bonus_speed) {
        this.attack = 5 + bonus_attack + FXGL.random(0, 3);
        this.defence = 3 + bonus_defence + FXGL.random(0, 3);
        this.speed = 20 + bonus_speed + FXGL.random(0, 5);

    }

    public int getAttack() {
        return this.attack;
    }

    public void getDamage(int damages) {
        this.life -= (damages - this.defence);
        if (life <= 0) {
            entity.removeFromWorld();
            Main.incrementBossBar(this.attack);
        }
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(10, 10));
        entity.getViewComponent().addChild(texture);
    }

    @Override
    public void onUpdate(double tpf) {
        Entity player = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).get(0);
        Point2D pos = player.getPosition();
        Point2D me = entity.getPosition();

        double x = pos.getX() - me.getX();
        double y = pos.getY() - me.getY();
        double movespeed = this.speed * tpf;
        if (player.distance(entity) <= 300) {
            movespeed += (40 * tpf);
        }
        entity.getComponent(PhysicsComponent.class).setLinearVelocity(x * movespeed, y * movespeed);

        AnimationChannel act_texture = texture.getAnimationChannel();
        x = x > y ? x : 0;
        if (x != 0) {
            if (x < -1 && (act_texture != animWalkLeft)) {
                getEntity().setScaleX(1);
                texture.loopAnimationChannel(animWalkLeft);
            } else if (x > 1 && (act_texture != animWalkLeft)) {
                getEntity().setScaleX(-1);
                texture.loopAnimationChannel(animWalkLeft);
            }
        } else if (y != 0) {
            if (y < -1 && (act_texture != animWalkUp)) {
                getEntity().setScaleX(1);
                texture.loopAnimationChannel(animWalkUp);
            } else if (y > 1 && (act_texture != animWalkDown)) {
                getEntity().setScaleX(1);
                texture.loopAnimationChannel(animWalkDown);
            }
        }
    }
}
