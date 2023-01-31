package App.Views;

import App.InputManager;
import App.Models.Menu;
import App.ViewManager;
import App.Views.User.UserMenu;

public class MainMenu extends ApplicationView
{
    @Override
    public Boolean show()
    {
        System.out.println("Witaj w publicznej pralni!");

        var menu = new Menu();
        menu.addItem("Korzystaj jako klient", () -> ViewManager.showView(UserMenu.class), true);
        menu.addItem("Korzystaj jako właściciel", () -> {
            System.out.println("Funkcja jeszcze nie jest zaimplementowana");
            InputManager.pressEnterToContinue();
        }, true);
        menu.addItem("Zakończ działanie aplikacji", () -> { }, false);

        menu.show();
        return menu.handle();
    }
}
