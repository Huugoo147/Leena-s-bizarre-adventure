package sample.first.Scenes;


import com.almasb.fxgl.app.AssetLoader;
import com.almasb.fxgl.audio.AudioPlayer;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class EndScreen extends SubScene {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    public EndScreen(boolean isWin) {

        final AudioPlayer ap = FXGL.getAudioPlayer();
        AssetLoader as = new AssetLoader();
        final Music sad = as.loadMusic("death_sound.mp3");
        ap.loopMusic(sad);
        Rectangle bg = new Rectangle(WIDTH, HEIGHT, Color.color(0, 0, 0, 0.85));
        bg.setStroke(Color.BLUE);
        bg.setStrokeWidth(1.75);
        bg.setEffect(new DropShadow(28, Color.color(0, 0, 0, 0.9)));

        HBox gradeBox = new HBox();
        VBox.setVgrow(gradeBox, Priority.ALWAYS);


        StackPane root = new StackPane(bg);
        root.setTranslateX(1280 / 2 - WIDTH / 2);
        root.setTranslateY(720 / 2 - HEIGHT / 2);

        Text textLevel = new Text("OVER");

        textLevel.setFont(Font.font(52));
        textLevel.setRotate(-20);
        textLevel.setFill(Color.ORANGE);
        textLevel.setStroke(Color.BLACK);
        textLevel.setStrokeWidth(3.5);
        textLevel.setTranslateX(root.getTranslateX() - textLevel.getLayoutBounds().getWidth() / 3);
        textLevel.setTranslateY(root.getTranslateY() + 25);
        Text comment = new Text();
        if (isWin)
            comment.setText(" Félicitations, tu nous \n as tous sauvé !");
        else
            comment.setText(" Tu es mort !\n Notre monde va périr !");
        comment.setFont(Font.font(48));
        comment.setTranslateX(340);
        comment.setTranslateY(240);
        comment.setStroke(Color.WHITE);

        Button recommence = new Button("Recommencer");
        recommence.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FXGL.getGameController().popSubScene();
                ap.stopMusic(sad);
                FXGL.getGameController().startNewGame();
            }
        });
        recommence.setTranslateX(350);
        recommence.setTranslateY(340);
        recommence.setStyle("-fx-underline: true;" + "-fx-background-color: transparent;" + "-fx-text-fill: white;" + "-fx-font-size: 4em;");


        Button exit = new Button("Partir");
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ap.stopMusic(sad);
                System.exit(0);
            }
        });
        exit.setTranslateX(750);
        exit.setTranslateY(340);
        exit.setStyle("-fx-underline: true;" + "-fx-background-color: transparent;" + "-fx-text-fill: white;" + "-fx-font-size: 4em;");


        getContentRoot().getChildren().addAll(root, textLevel, comment, recommence, exit);
    }
}
