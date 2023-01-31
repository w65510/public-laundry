package App.Views.User;

import App.InputManager;
import App.Models.Menu;
import App.ViewManager;
import App.Views.ApplicationView;

public class UserMenu extends ApplicationView
{
    @Override
    public Boolean show()
    {
        System.out.println("Co chcesz zrobić?");

        var menu = new Menu();
        menu.addItem("Wyświetl pralki", () -> ViewManager.showView(UserWashers.class), true);
        menu.addItem("Wyświetl suszarki", () -> {
            System.out.println("Funkcja jeszcze nie zaimplementowana");
            InputManager.pressEnterToContinue();
        }, true);
        menu.addItem("Powrót", () -> { }, false);

        menu.show();
        return menu.handle();
    }
}
