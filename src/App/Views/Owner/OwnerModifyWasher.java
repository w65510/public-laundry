package App.Views.Owner;

import App.InputManager;
import App.Views.ApplicationView;
import Infrastructure.Db.QueryManager;

import java.sql.SQLException;
import java.util.Optional;

public class OwnerModifyWasher extends ApplicationView
{
    @Override
    public Boolean show() throws SQLException
    {
        var washer = QueryManager.getCurrentOwnerWasher();

        System.out.println("Modyfikowanie parametrów pralki\n");

        washer.Name = InputManager.getStringPrompt("(Max 18) Podaj nową nazwę pralki [" + washer.Name +"]", Optional.ofNullable(x -> x.length() > 0 && x.length() <= 18));
        washer.Price = InputManager.getIntPrompt("(Min 0) Podaj nową cenę za kg prania [" + washer.Price +"zł/kg]", Optional.ofNullable(x -> x >= 0));
        washer.MaxCapacity = InputManager.getIntPrompt("(Min 1) Podaj nową ładowność pralki [" + washer.MaxCapacity + "]", Optional.ofNullable(x -> x >= 1));

        QueryManager.updateWasher(washer);

        System.out.println("\nPralka została zaktualizowana!");
        InputManager.pressEnterToContinue();

        return false;
    }
}
