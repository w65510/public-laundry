package App.Views.User;

import App.GlobalVariables;
import App.InputManager;
import App.Models.Menu;
import App.ViewManager;
import App.Views.ApplicationView;
import Infrastructure.Db.QueryManager;

import java.sql.SQLException;

public class UserDryersView extends ApplicationView
{
    @Override
    public Boolean show() throws SQLException
    {
        if (!anyWorkingDryer())
            return false;

        var dryers = QueryManager.getUserDryers();
        System.out.println("Którą suszarkę chcesz wybrać?");

        var menu = new Menu();

        for (var i = 0; i < dryers.size(); i++) {
            var dryer = dryers.get(i);
            menu.addItem(dryer.toString(), () -> {
                GlobalVariables.SelectedDryer = dryer.Id;
                ViewManager.showView(UserDryerManageView.class);
                GlobalVariables.SelectedDryer = -1;
            }, true);
        }

        menu.addItem("Odśwież", () -> { }, true);
        menu.addItem("Powrót", () -> { }, false);

        menu.show();
        return menu.handle();
    }

    private Boolean anyWorkingDryer() throws SQLException
    {
        var washersCount = QueryManager.getDryersCount("is_broken = false");
        var anyWorking = washersCount != 0;

        if (!anyWorking) {
            System.out.println("Przepraszamy, w pralni nie znajduje się żadna sprawna suszarka.");
            InputManager.pressEnterToContinue();
        }

        return anyWorking;
    }
}
