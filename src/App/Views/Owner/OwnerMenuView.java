package App.Views.Owner;

import App.Models.Menu;
import App.ViewManager;
import App.Views.ApplicationView;
import App.Views.User.UserDryersView;
import App.Views.User.UserWashersView;

import java.sql.SQLException;

public class OwnerMenuView extends ApplicationView
{
    @Override
    public Boolean show() throws SQLException
    {
        System.out.println("Co chcesz zrobić?");

        var menu = new Menu();
        menu.addItem("Wyświetl pralki", () -> ViewManager.showView(OwnerWashersView.class), true);
        menu.addItem("Wyświetl suszarki", () -> ViewManager.showView(OwnerDryersView.class), true);
        menu.addItem("Powrót", () -> { }, false);

        menu.show();
        return menu.handle();
    }
}
