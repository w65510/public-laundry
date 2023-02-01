package App.Views.User;

import App.Models.Menu;
import App.ViewManager;
import App.Views.ApplicationView;

public class UserMenuView extends ApplicationView
{
    @Override
    public Boolean show()
    {
        System.out.println("Co chcesz zrobić?");

        var menu = new Menu();
        menu.addItem("Wyświetl pralki", () -> ViewManager.showView(UserWashersView.class), true);
        menu.addItem("Wyświetl suszarki", () -> ViewManager.showView(UserDryersView.class), true);
        menu.addItem("Powrót", () -> { }, false);

        menu.show();
        return menu.handle();
    }
}
