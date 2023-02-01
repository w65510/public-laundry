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
        var washers = QueryManager.getOwnerWashers();

        if (washers.size() == 0)
            System.out.println("Nie posiadasz żadnych pralek!");
        else
            System.out.println("Którą pralkę chcesz wybrać?");

        var menu = new Menu();

        for (var i = 0; i < washers.size(); i++) {
            var washer = washers.get(i);
            menu.addItem(washer.toString(), () -> {
                GlobalVariables.SelectedWasher = washer.Id;
                ViewManager.showView(OwnerWasherManageView.class);
                GlobalVariables.SelectedWasher = -1;
            }, true);
        }

        menu.addItem("Dodaj nową pralkę", null, true);
        menu.addItem("Odśwież", () -> { }, true);
        menu.addItem("Powrót", () -> { }, false);

        menu.show();
        return menu.handle();
    }
}
