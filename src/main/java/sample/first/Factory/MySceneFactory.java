package sample.first.Factory;

import com.almasb.fxgl.app.FXGLMenu;
import com.almasb.fxgl.app.MenuType;
import com.almasb.fxgl.app.SceneFactory;
import sample.first.Scenes.MyMainMenu;

public class MySceneFactory extends SceneFactory {

    @Override
    public FXGLMenu newMainMenu() {
        return new MyMainMenu.MyMenu(MenuType.MAIN_MENU);
    }

    @Override
    public FXGLMenu newGameMenu() {
        return new MyMainMenu.MyMenu(MenuType.GAME_MENU);
    }

}
