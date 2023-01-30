package Infrastructure.Db;

import App.Models.UserWasher;
import Domain.Entities.Washer;

import java.sql.SQLException;
import java.util.ArrayList;

public class QueryManager
{
    public static int getWashersCount(String where) throws SQLException
    {
        var sql = "SELECT COUNT(*) FROM " + Washer.TableName + " ";

        if (where != null)
            sql += "WHERE " + where;

        var conn = DatabaseContext.getConnection();
        var cmd = conn.createStatement();
        var res = cmd.executeQuery(sql);

        res.next();
        var count = res.getInt(1);
        cmd.close();

        DatabaseContext.closeConnection(conn);
        return count;
    }

    public static ArrayList<UserWasher> getUserWashers() throws SQLException
    {
        var sql = """
                SELECT w.id, name, price, maxCapacity, COUNT(pickup_date is null) FROM %s w
                                LEFT JOIN washing_process wp
                                ON w.id = wp.washer_id
                				WHERE w.is_broken = false
                				GROUP BY w.id, name, price, maxCapacity;
                """.formatted(Washer.TableName);

        var conn = DatabaseContext.getConnection();
        var cmd = conn.createStatement();
        var res = cmd.executeQuery(sql);

        var washers = new ArrayList<UserWasher>();

        while (res.next()) {
            var washer = new UserWasher();

            washer.Id = res.getInt(1);
            washer.Name = res.getString(2);
            washer.Price = res.getInt(3);
            washer.MaxCapacity = res.getInt(4);
            washer.InProgress = res.getInt(5) > 0;

            washers.add(washer);
        }

        cmd.close();
        DatabaseContext.closeConnection(conn);

        return washers;
    }
}
