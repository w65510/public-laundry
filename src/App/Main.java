package App;

import App.Views.MainMenu;
import Infrastructure.Db.DatabaseContext;

public class Main
{
    public static void main(String[] args)
    {
        if (!DatabaseContext.isDatabaseCreated())
            DatabaseContext.createDatabase();

        ViewManager.showView(MainMenu.class);
    }
}
