package App.Views.User;

import App.Extensions;
import App.InputManager;
import App.Models.Menu;
import App.ViewManager;
import App.Views.ApplicationView;
import Infrastructure.Db.QueryManager;

import java.sql.SQLException;

public class UserDryerManageView extends ApplicationView
{
    @Override
    public Boolean show() throws SQLException
    {
        var dryer = QueryManager.getCurrentUserDryer();

        if (dryer == null)
            return false;

        System.out.println("Co chcesz zrobić z suszarką " + dryer.Name + "?");

        var menu = new Menu();

        if (dryer.EndDate == null) {
            menu.addItem("Wysusz pranie", () -> ViewManager.showView(DryLaundryView.class), false);
        }
        else if(Extensions.isInPast(dryer.EndDate)) {
            System.out.println("Suszarka zakończyła działanie, znajdujące się w niej pranie:");
            showLaundry();

            menu.addItem("Odbierz wysuszone pranie", () -> ViewManager.showView(PickupLaundryView.class), true);
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
        var laundry = QueryManager.getCurrentDryerLaundry();

        for (var laund:laundry)
            System.out.println(laund);
    }
}
