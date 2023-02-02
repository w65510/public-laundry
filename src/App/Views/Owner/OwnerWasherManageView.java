package App.Views.Owner;

import App.Extensions;
import App.GlobalVariables;
import App.InputManager;
import App.Models.Menu;
import App.ViewManager;
import App.Views.ApplicationView;
import App.Views.User.DoLaundryView;
import App.Views.User.PickupLaundryView;
import Infrastructure.Db.QueryManager;

import java.sql.SQLException;

public class OwnerWasherManageView extends ApplicationView
{
    @Override
    public Boolean show() throws SQLException
    {
        var washer = QueryManager.getCurrentOwnerWasher();

        if (washer == null)
            return false;

        System.out.println("Co chcesz zrobić z pralką " + washer.Name + "?");

        var menu = new Menu();
        menu.addItem("Pokaż statystyki", () -> ViewManager.showView(OwnerWasherStatisticsView.class), true);

        if (washer.EndDate == null) {
            menu.addItem("Sprzedaj pralkę", () -> sellWasher(), false);
            menu.addItem("Modyfikuj parametry", () -> ViewManager.showView(OwnerModifyWasher.class), true);
        }
        else if(Extensions.isInPast(washer.EndDate)) {
            System.out.println("\nPralka zakończyła działanie (Kod odbioru: " + washer.PickupCode + ")\nZnajdujące się w niej pranie:");
            showLaundry();

            menu.addItem("Odbierz pranie", () -> ViewManager.showView(PickupLaundryView.class), true);
        }
        else {
            System.out.println("\nPralka pracuje (Kod odbioru: " + washer.PickupCode + ")\nZnajdujące się w niej pranie:");
            showLaundry();

            menu.addItem("Przerwij pranie", () -> stopWashing(), true);
        }

        if (washer.IsBroken)
            menu.addItem("Napraw pralkę", () -> fixWasher(), true);

        menu.addItem("Powrót", () -> { }, false);

        menu.show();
        return menu.handle();
    }

    private void stopWashing()
    {
        QueryManager.stopCurrentWasher();

        System.out.println("Pralka została zatrzymana");
        InputManager.pressEnterToContinue();
    }

    private void fixWasher()
    {
        QueryManager.fixWasher(GlobalVariables.SelectedWasher);

        System.out.println("Pralka została naprawiona");
        InputManager.pressEnterToContinue();
    }

    private void sellWasher()
    {
        QueryManager.deleteWasher(GlobalVariables.SelectedWasher);

        System.out.println("Pralka została sprzedana");
        InputManager.pressEnterToContinue();
    }

    private void showLaundry() {
        System.out.println();
        var laundry = QueryManager.getCurrentWasherLaundry();

        for (var laund:laundry)
            System.out.println(laund);
    }
}
