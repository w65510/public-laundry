package App.Views.Owner;

import App.GlobalVariables;
import App.InputManager;
import App.Models.Menu;
import App.ViewManager;
import App.Views.ApplicationView;
import App.Views.User.UserDryerManageView;
import Infrastructure.Db.QueryManager;

import java.sql.SQLException;

public class OwnerDryersView extends ApplicationView
{
    @Override
    public Boolean show() throws SQLException
    {
        var dryers = QueryManager.getOwnerDryers();

        if (dryers.size() == 0)
            System.out.println("Nie posiadasz żadnych suszarek!");
        else
            System.out.println("Którą suszarkę chcesz wybrać?");

        var menu = new Menu();

        for (var i = 0; i < dryers.size(); i++) {
            var dryer = dryers.get(i);
            menu.addItem(dryer.toString(), () -> {
                GlobalVariables.SelectedDryer = dryer.Id;
                ViewManager.showView(OwnerDryerManageView.class);
                GlobalVariables.SelectedDryer = -1;
            }, true);
        }

        menu.addItem("Dodaj nową suszarkę", () -> ViewManager.showView(AddNewDryerView.class), true);
        menu.addItem("Odśwież", () -> { }, true);
        menu.addItem("Powrót", () -> { }, false);

        menu.show();
        return menu.handle();
    }
}
