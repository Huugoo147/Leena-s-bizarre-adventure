package sample.first.Entites;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import sample.first.EntityType;
import sample.first.Player.Player;

public class Bonus extends Component {
    private AnimatedTexture texture;
    private AnimationChannel animIdle;

    public Bonus() {
        animIdle = new AnimationChannel(FXGL.image("bonuses.png"),
                6, 32, 23, Duration.millis(4000), 0, 5);
        texture = new AnimatedTexture(animIdle);
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(10, 10));
        entity.getViewComponent().addChild(texture);
        texture.loopAnimationChannel(animIdle);
    }

    public void getBonus() {
        int random = FXGL.random(1, 8);
        Player player = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).get(0).getComponent(Player.class);
        switch (random) {
            case (1):
                player.addPotion();
                break;
            case (2):
                player.getDamage(30);
                break;
            case (3):
                player.incrementAttack(-4);
                break;
            case (4):
                player.incrementSpeed(1000);
                break;
            case (5):
                player.incrementSpeed(-2000);
                break;
            case (6):
                player.incrementDefence(-5);
                break;
            case (7):
                player.incrementDefence(5);
                break;
            case (8):
                player.incrementAttack(4);
                break;
        }
        FXGL.play("bonus.mp3");
        entity.removeFromWorld();
    }
}
