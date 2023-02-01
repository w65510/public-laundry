package App.Views.Owner;

import App.InputManager;
import App.Views.ApplicationView;
import Infrastructure.Db.QueryManager;

import java.sql.SQLException;

public class OwnerWasherStatisticsView extends ApplicationView
{
    @Override
    public Boolean show() throws SQLException
    {
        var statistics = QueryManager.getCurrentOwnerWasherStatistics();

        System.out.println("Statystyki pralki\n");
        System.out.println(statistics);

        InputManager.pressEnterToContinue();

        return false;
    }
}
