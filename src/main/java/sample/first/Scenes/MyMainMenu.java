package sample.first.Scenes;

import com.almasb.fxgl.app.FXGLMenu;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import javafx.beans.binding.StringBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import sample.first.Factory.MySceneFactory;


public class MyMainMenu extends GameApplication {
    @Override
    protected void initSettings(GameSettings settings) {

        settings.setExperimentalNative(true);
        settings.setTitle("ALLO");
        settings.setSceneFactory(new MySceneFactory());
        settings.setMenuEnabled(true);
        settings.setCloseConfirmation(false);
        settings.setWidth(1280);
        settings.setHeight(700);
    }


    public static class MyMenu extends FXGLMenu {

        public MyMenu(MenuType type) {
            super(type);
            Button btnStart = new Button("Start");
            btnStart.setPadding(new Insets(0, 20, 0, 20));
            Texture brick = FXGL.getAssetLoader().loadTexture("./bg.jpg");
            brick.setTranslateX(0);
            brick.setTranslateY(0);
            btnStart.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    fireNewGame();
                }
            });
            btnStart.setTranslateX(200);
            btnStart.setTranslateY(400);
            String style = "-fx-padding: 8 15 15 15;\n" +
                    "    -fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;\n" +
                    "    -fx-background-radius: 8;\n" +
                    "    -fx-background-color: \n" +
                    "        linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%),\n" +
                    "        #9d4024,\n" +
                    "        #d86e3a,\n" +
                    "        radial-gradient(center 50% 50%, radius 100%, #d86e3a, #c54e2c);\n" +
                    "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.75) , 4,0,0,1 );\n" +
                    "    -fx-font-weight: bold;\n" +
                    "    -fx-font-size: 4em";
            btnStart.setStyle(style);

            Text title = new Text("Leena's Bizarre Adventure");
            title.setTranslateX(250);
            title.setTranslateY(200);
            title.setStyle("    -fx-font-size: 5em;\n" +
                    "    -fx-font-family: \"Segoe UI Semibold\";\n" +
                    "    -fx-opacity: 100;");
            title.setStroke(Color.WHITE);

            Button btnMulit = new Button("Multiplayer");
            btnMulit.setStyle(style);
            btnMulit.setTranslateY(400);
            btnMulit.setTranslateX(500);
            Button btnExit = new Button("Exit");
            btnExit.setStyle(style);
            btnExit.setTranslateY(400);
            btnExit.setTranslateX(1000);
            btnExit.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    System.exit(0);
                }
            });
            this.addUINode(brick);
            this.addUINode(title);
            this.addUINode(btnStart);
            this.addUINode(btnExit);
            this.addUINode(btnMulit);
        }


        @Override
        protected Button createActionButton(String name, Runnable action) {
            return new Button(name);
        }

        @Override
        protected Button createActionButton(StringBinding name, Runnable action) {
            return new Button(name.get());
        }

        @Override
        protected Node createBackground(double width, double height) {
            return new Rectangle(width, height, Color.WHITE);
        }

        @Override
        protected Node createTitleView(String title) {
            return new Text(title);
        }

        @Override
        protected Node createVersionView(String version) {
            return new Text(version);
        }

        @Override
        protected Node createProfileView(String profileName) {
            return new Text(profileName);
        }


    }

    public static void main(String[] args) {
        launch(args);
    }
}