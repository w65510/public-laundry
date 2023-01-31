package App.Views.User;

import App.Extensions;
import App.GlobalVariables;
import App.InputManager;
import App.Models.Menu;
import App.ViewManager;
import App.Views.ApplicationView;
import Infrastructure.Db.QueryManager;

import java.sql.SQLException;
import java.util.stream.Collectors;

public class UserWasherManage extends ApplicationView
{
    @Override
    public Boolean show() throws SQLException
    {
        var washer = QueryManager.getCurrentUserWasher();

        if (washer == null)
            return false;

        System.out.println("Co chcesz zrobić z pralką " + washer.Name + "?");

        var menu = new Menu();

        if (washer.EndDate == null) {
            menu.addItem("Zrób pranie", () -> ViewManager.showView(DoLaundry.class), false);
        }
        else if(Extensions.isInPast(washer.EndDate)) {
            menu.addItem("Odbierz pranie", () -> {
                System.out.println("Funkcja jeszcze nie zaimplementowana");
                InputManager.pressEnterToContinue();
            }, true);
        }
        else {
            System.out.println("Pralka pracuje, brak dostępnych operacji");
            InputManager.pressEnterToContinue();
            return false;
        }

        menu.addItem("Powrót", () -> { }, false);

        menu.show();
        return menu.handle();
    }
}
