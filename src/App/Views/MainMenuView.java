package App.Views;

import App.Models.Menu;
import App.ViewManager;
import App.Views.Owner.OwnerMenuView;
import App.Views.User.UserMenuView;

public class MainMenuView extends ApplicationView
{
    @Override
    public Boolean show()
    {
        System.out.println("Witaj w publicznej pralni!");

        var menu = new Menu();
        menu.addItem("Korzystaj jako klient", () -> ViewManager.showView(UserMenuView.class), true);
        menu.addItem("Korzystaj jako właściciel", () -> ViewManager.showView(OwnerMenuView.class), true);
        menu.addItem("Zakończ działanie aplikacji", () -> { }, false);

        menu.show();
        return menu.handle();
    }
}
