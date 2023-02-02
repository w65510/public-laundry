package App.Views.Owner;

import App.InputManager;
import App.Views.ApplicationView;
import Infrastructure.Db.QueryManager;

import java.sql.SQLException;
import java.util.Optional;

public class AddNewDryerView extends ApplicationView
{
    @Override
    public Boolean show() throws SQLException
    {
        System.out.println("Wprowadź parametry nowej suszarki\n");

        var name = InputManager.getStringPrompt("(Max 18) Podaj nazwę suszarki", Optional.ofNullable(x -> x.length() > 0 && x.length() <= 18));
        var price = InputManager.getIntPrompt("(Min 0) Podaj cenę za kg suszenia", Optional.ofNullable(x -> x >= 0));
        var maxCapacity = InputManager.getIntPrompt("(Min 1) Podaj ładowność suszarki", Optional.ofNullable(x -> x >= 1));

        QueryManager.createDryer(name, price, maxCapacity);

        System.out.println("\nSuszarka została dodana!");
        InputManager.pressEnterToContinue();

        return false;
    }
}
