package App.Views.User;

import App.GlobalVariables;
import App.InputManager;
import App.Models.Menu;
import App.ViewManager;
import App.Views.ApplicationView;
import Infrastructure.Db.QueryManager;

import java.sql.SQLException;

public class UserWashersView extends ApplicationView
{
    @Override
    public Boolean show() throws SQLException
    {
        if (!anyWorkingWasher())
            return false;

        System.out.println("Którą pralkę chcesz wybrać?");
        var washers = QueryManager.getUserWashers();

        var menu = new Menu();

        for (var i = 0; i < washers.size(); i++) {
            var washer = washers.get(i);
            menu.addItem(washer.toString(), () -> {
                GlobalVariables.SelectedWasher = washer.Id;
                ViewManager.showView(UserWasherManageView.class);
                GlobalVariables.SelectedWasher = -1;
            }, true);
        }

        menu.addItem("Odśwież", () -> { }, true);
        menu.addItem("Powrót", () -> { }, false);

        menu.show();
        return menu.handle();
    }

    private Boolean anyWorkingWasher() throws SQLException
    {
        var washersCount = QueryManager.getWashersCount("is_broken = false");
        var anyWorking = washersCount != 0;

        if (!anyWorking) {
            System.out.println("Przepraszamy, w pralni nie znajduje się żadna sprawna pralka.");
            InputManager.pressEnterToContinue();
        }

        return anyWorking;
    }
}
