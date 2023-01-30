package App.Views;

import App.GlobalVariables;
import App.InputManager;
import Infrastructure.Db.QueryManager;

import java.sql.SQLException;

public class UserWashers extends ApplicationView
{
    @Override
    public Boolean show() throws SQLException
    {
        if (!anyWorkingWasher())
            return false;

        System.out.println("Którą pralkę chcesz wybrać?");
        System.out.println();

        var washers = QueryManager.getUserWashers();
        for (var i = 0; i < washers.size(); i++) {
            var string = (i+1) + ". " + washers.get(i).toString();
            System.out.println(string);
        }

        System.out.println(washers.size()+1 + ". Powrót");
        var choice = getChoice(washers.size()+1);

        if (choice == washers.size()+1)
            return false;

        GlobalVariables.SelectedWasher = washers.get(choice-1).Id;
        System.out.println("Funkcja jeszcze nie zaimplementowana");
        InputManager.pressEnterToContinue();

        return true;
    }

    private Boolean anyWorkingWasher() throws SQLException
    {
        var washersCount = QueryManager.getWashersCount("is_broken = false");
        var anyWorking = washersCount != 0;

        if (!anyWorking) {
            System.out.println("Przepraszamy, w pralni nie znajduje się żadna sprawna pralka.");
            InputManager.pressEnterToContinue();
        }

        return anyWorking;
    }
}
