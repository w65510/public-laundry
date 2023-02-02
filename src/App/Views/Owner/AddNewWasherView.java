package App.Views.Owner;

import App.InputManager;
import App.Views.ApplicationView;
import Infrastructure.Db.QueryManager;

import java.sql.SQLException;
import java.util.Optional;

public class AddNewWasherView extends ApplicationView
{
    @Override
    public Boolean show() throws SQLException
    {
        System.out.println("Wprowadź parametry nowej pralki\n");

        var name = InputManager.getStringPrompt("(Max 18) Podaj nazwę pralki", Optional.ofNullable(x -> x.length() > 0 && x.length() <= 18));
        var price = InputManager.getIntPrompt("(Min 0) Podaj cenę za kg prania", Optional.ofNullable(x -> x >= 0));
        var maxCapacity = InputManager.getIntPrompt("(Min 1) Podaj ładowność pralki", Optional.ofNullable(x -> x >= 1));

        QueryManager.createWasher(name, price, maxCapacity);

        System.out.println("\nPralka została dodana!");
        InputManager.pressEnterToContinue();

        return false;
    }
}
