package App.Views.Owner;

import App.Extensions;
import App.GlobalVariables;
import App.InputManager;
import App.Models.Menu;
import App.ViewManager;
import App.Views.ApplicationView;
import App.Views.User.PickupLaundryView;
import Infrastructure.Db.QueryManager;

import java.sql.SQLException;

public class OwnerDryerManageView extends ApplicationView
{
    @Override
    public Boolean show() throws SQLException
    {
        var dryer = QueryManager.getCurrentOwnerDryer();

        if (dryer == null)
            return false;

        System.out.println("Co chcesz zrobić z suszarką " + dryer.Name + "?");

        var menu = new Menu();
        menu.addItem("Pokaż statystyki", () -> ViewManager.showView(OwnerDryerStatisticsView.class), true);

        if (dryer.EndDate == null) {
            menu.addItem("Sprzedaj suszarkę", () -> sellDryer(), false);
            menu.addItem("Modyfikuj parametry", () -> ViewManager.showView(OwnerModifyDryer.class), true);
        }
        else if(Extensions.isInPast(dryer.EndDate)) {
            System.out.println("\nSuszarka zakończyła działanie (Kod odbioru: " + dryer.PickupCode + ")\nZnajdujące się w niej pranie:");
            showLaundry();

            menu.addItem("Odbierz wysuszone pranie", () -> ViewManager.showView(PickupLaundryView.class), true);
        }
        else {
            System.out.println("\nSuszarka pracuje (Kod odbioru: " + dryer.PickupCode + ")\nZnajdujące się w niej pranie:");
            showLaundry();

            menu.addItem("Przerwij suszenie", () -> stopDrying(), true);
        }

        if (dryer.IsBroken)
            menu.addItem("Napraw suszarkę", () -> fixDryer(), true);

        menu.addItem("Powrót", () -> { }, false);

        menu.show();
        return menu.handle();
    }

    private void stopDrying()
    {
        QueryManager.stopCurrentDryer();

        System.out.println("Suszarka została zatrzymana");
        InputManager.pressEnterToContinue();
    }

    private void fixDryer()
    {
        QueryManager.fixDryer(GlobalVariables.SelectedDryer);

        System.out.println("Suszarka została naprawiona");
        InputManager.pressEnterToContinue();
    }

    private void sellDryer()
    {
        QueryManager.deleteDryer(GlobalVariables.SelectedDryer);

        System.out.println("Suszarka została sprzedana");
        InputManager.pressEnterToContinue();
    }

    private void showLaundry() {
        System.out.println();
        var laundry = QueryManager.getCurrentDryerLaundry();

        for (var laund:laundry)
            System.out.println(laund);
    }
}
