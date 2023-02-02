package App.Views.Owner;

import App.InputManager;
import App.Views.ApplicationView;
import Infrastructure.Db.QueryManager;

import java.sql.SQLException;
import java.util.Optional;

public class OwnerModifyDryer extends ApplicationView
{
    @Override
    public Boolean show() throws SQLException
    {
        var dryer = QueryManager.getCurrentOwnerDryer();

        System.out.println("Modyfikowanie parametrów suszarki\n");

        dryer.Name = InputManager.getStringPrompt("(Max 18) Podaj nową nazwę suszarki [" + dryer.Name +"]", Optional.ofNullable(x -> x.length() > 0 && x.length() <= 18));
        dryer.Price = InputManager.getIntPrompt("(Min 0) Podaj nową cenę za kg suszenia [" + dryer.Price +"zł/kg]", Optional.ofNullable(x -> x >= 0));
        dryer.MaxCapacity = InputManager.getIntPrompt("(Min 1) Podaj nową ładowność suszarki [" + dryer.MaxCapacity + "]", Optional.ofNullable(x -> x >= 1));

        QueryManager.updateDryer(dryer);

        System.out.println("\nSuszarka została zaktualizowana!");
        InputManager.pressEnterToContinue();

        return false;
    }
}
