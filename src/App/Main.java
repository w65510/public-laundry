package App;

import App.Views.MainMenuView;
import Infrastructure.Db.DatabaseContext;

public class Main
{
    public static void main(String[] args)
    {
        if (!DatabaseContext.isDatabaseCreated())
            DatabaseContext.createDatabase();

        ViewManager.showView(MainMenuView.class);
        InputManager.clearConsole();
    }
}
