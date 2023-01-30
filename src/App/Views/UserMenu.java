package App.Views;

import App.InputManager;
import App.ViewManager;

public class UserMenu extends ApplicationView
{
    @Override
    public Boolean show()
    {
        System.out.println("Co chcesz zrobić?");
        System.out.println();
        System.out.println("1. Wyświetl pralki");
        System.out.println("2. Wyświetl suszarki");
        System.out.println("3. Powrót");

        var choice = getChoice(3);

        switch (choice) {
            case 1:
                ViewManager.showView(UserWashers.class);
                break;
            case 2:
                System.out.println("Funkcja jeszcze nie zaimplementowana");
                InputManager.pressEnterToContinue();
                break;
            case 3:
                return false;
        }

        return true;
    }
}
