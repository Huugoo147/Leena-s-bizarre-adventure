package sample.first.Player;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.ui.Position;
import com.almasb.fxgl.ui.ProgressBar;
import javafx.scene.paint.Color;

public class PlayerHealthBar {
    private ProgressBar bar;

    public PlayerHealthBar() {
        ProgressBar health = new ProgressBar(true);
        health.setCurrentValue(100);
        health.setMaxValue(100);
        health.setFill(javafx.scene.paint.Color.GREEN);
        health.setScaleX(1.5);
        health.setScaleY(2);
        health.setTranslateX(920);
        health.setTranslateY(660);
        health.setLabelVisible(true);
        health.setLabelFill(Color.BLACK);
        health.setTraceFill(Color.RED);
        health.setLabelPosition(Position.LEFT);
        FXGL.getGameState().setValue("health", health);
        FXGL.getGameScene().addUINode(health);
        this.bar = health;
    }

    public ProgressBar getBar() {
        return this.bar;
    }

    public void setBar(ProgressBar bar) {
        this.bar = bar;
    }

}
