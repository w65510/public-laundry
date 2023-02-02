package App.Views.Owner;

import App.InputManager;
import App.Views.ApplicationView;
import Infrastructure.Db.QueryManager;

import java.sql.SQLException;

public class OwnerDryerStatisticsView extends ApplicationView
{
    @Override
    public Boolean show() throws SQLException
    {
        var statistics = QueryManager.getCurrentOwnerDryerStatistics();

        System.out.println("Statystyki suszarki\n");
        System.out.println(statistics);

        InputManager.pressEnterToContinue();

        return false;
    }
}
