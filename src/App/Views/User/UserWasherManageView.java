package App.Views.User;

import App.Extensions;
import App.InputManager;
import App.Models.Menu;
import App.ViewManager;
import App.Views.ApplicationView;
import Infrastructure.Db.QueryManager;

import java.sql.SQLException;

public class UserWasherManageView extends ApplicationView
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
            menu.addItem("Zrób pranie", () -> ViewManager.showView(DoLaundryView.class), false);
        }
        else if(Extensions.isInPast(washer.EndDate)) {
            System.out.println("Pralka zakończyła działanie, znajdujące się w niej pranie:");
            showLaundry();

            menu.addItem("Odbierz pranie", () -> ViewManager.showView(PickupLaundryView.class), true);
        }
        else {
            System.out.println("Pralka pracuje, znajdujące się w niej pranie:");
            showLaundry();

            System.out.println("\nBrak dostępnych operacji");
            InputManager.pressEnterToContinue();
            return false;
        }

        menu.addItem("Powrót", () -> { }, false);

        menu.show();
        return menu.handle();
    }

    private void showLaundry() {
        System.out.println();
        var laundry = QueryManager.getCurrentWasherLaundry();

        for (var laund:laundry)
            System.out.println(laund);
    }
}
