package App.Views;

import App.InputManager;
import App.ViewManager;
import jdk.jshell.spi.ExecutionControl;

public class MainMenu extends ApplicationView
{
    @Override
    public Boolean show()
    {
        System.out.println("Witaj w publicznej pralni!");
        System.out.println();
        System.out.println("1. Korzystaj jako klient");
        System.out.println("2. Korzystaj jako właściciel");
        System.out.println("3. Zakończ działanie aplikacji");

        var choice = getChoice(3);

        switch (choice){
            case 1:
                ViewManager.showView(UserMenu.class);
                break;
            case 2:
                System.out.println("Funkcja jeszcze nie jest zaimplementowana");
                InputManager.pressEnterToContinue();
                break;
            case 3:
                return false;
        }

        return true;
    }
}
