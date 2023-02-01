package App.Views.Owner;

import App.GlobalVariables;
import App.Models.Menu;
import App.ViewManager;
import App.Views.ApplicationView;
import App.Views.User.UserDryerManageView;
import Infrastructure.Db.QueryManager;

import java.sql.SQLException;

public class OwnerWashersView extends ApplicationView
{
    @Override
    public Boolean show() throws SQLException
    {
        var washers = QueryManager.getUserDryers();
        System.out.println("Którą suszarkę chcesz wybrać?");

        var menu = new Menu();

        for (var i = 0; i < washers.size(); i++) {
            var washer = washers.get(i);
            menu.addItem(washer.toString(), () -> {
                GlobalVariables.SelectedDryer = washer.Id;
                ViewManager.showView(UserDryerManageView.class);
                GlobalVariables.SelectedDryer = -1;
            }, true);
        }

        menu.addItem("Odśwież", () -> { }, true);
        menu.addItem("Powrót", () -> { }, false);

        menu.show();
        return menu.handle();
    }
}
