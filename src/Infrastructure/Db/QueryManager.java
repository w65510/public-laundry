package Infrastructure.Db;

import App.Extensions;
import App.GlobalVariables;
import App.Models.UserWasher;
import Domain.Entities.Laundry;
import Domain.Entities.Washer;
import Domain.Entities.WashingProcess;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

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

    public static UserWasher getCurrentUserWasher() throws SQLException
    {
        var washers = QueryManager.getUserWashers().stream().filter(x -> x.Id == GlobalVariables.SelectedWasher).collect(Collectors.toList());

        if (washers.size() == 0)
            return null;

        return washers.get(0);
    }

    public static String createCurrentWasherLaundry(ArrayList<Laundry> userLaundry)
    {
        try
        {
            // Create process
            var washer = getCurrentUserWasher();

            var laundryWeight = userLaundry.stream().mapToInt(x -> x.weight).sum();
            var washCost = laundryWeight * washer.Price;
            var endDate = LocalDateTime.now().plus(Duration.ofSeconds(laundryWeight * 5));
            var pickupCode = Extensions.generatePickupCode();

            var sql = """
                INSERT INTO %1$s (cost, washer_id, end_date, pickup_code)
                VALUES (%2$d, %3$d, '%4$s', '%5$s')
                RETURNING id;
                """.formatted(WashingProcess.TableName, washCost, washer.Id, endDate, pickupCode);

            var conn = DatabaseContext.getConnection();
            Statement cmd = conn.createStatement();
            var res = cmd.executeQuery(sql);

            res.next();
            var processId = res.getInt(1);

            // Create laundry
            StringBuilder builder = new StringBuilder("INSERT INTO %1$s (name, weight) VALUES\n".formatted(Laundry.TableName));

            for (var i = 0; i < userLaundry.size(); i++) {
                var laundry = userLaundry.get(i);
                if (i > 0)
                    builder.append(", \n");

                builder.append("('%1$s', %2$d)".formatted(laundry.name, laundry.weight));
            }

            builder.append("\nRETURNING id;");
            sql = builder.toString();
            res = cmd.executeQuery(sql);

            var laundryId = new ArrayList<Integer>();
            while (res.next())
                laundryId.add(res.getInt(1));

            // Create n-n relation
            builder.setLength(0);
            builder.append("INSERT INTO %1$s (process_id, laundry_id) VALUES\n".formatted("washing_laundry"));

            for (var i = 0; i < laundryId.size(); i++) {
                var id = laundryId.get(i);
                if (i > 0)
                    builder.append(", \n");

                builder.append("(%1$d, %2$d)".formatted(processId, id));
            }
            builder.append(";");

            sql = builder.toString();
            cmd.execute(sql);

            cmd.close();
            DatabaseContext.saveChanges(conn, true);

            return pickupCode;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void setCurrentWasherBroken()
    {
        var sql = """
                UPDATE %1$s
                SET is_broken = true
                WHERE id = %2$s
                
                """.formatted(Washer.TableName, GlobalVariables.SelectedWasher);

        var conn = DatabaseContext.getConnection();
        Statement cmd = null;
        try
        {
            cmd = conn.createStatement();
            cmd.execute(sql);
            cmd.close();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        DatabaseContext.saveChanges(conn, true);
    }

    public static ArrayList<UserWasher> getUserWashers() throws SQLException
    {
        var sql = """
                SELECT w.id, name, price, maxCapacity,
                (
                    SELECT end_date FROM %2$s
                    WHERE washer_id = w.id
                    AND pickup_date is null
                ) as busy
                FROM %1$s w
                LEFT JOIN washing_process wp
                ON w.id = wp.washer_id
                WHERE w.is_broken = false
                GROUP BY w.id, name, price, maxCapacity;
                """.formatted(Washer.TableName, WashingProcess.TableName);

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
            washer.EndDate = res.getTimestamp(5);

            washers.add(washer);
        }

        cmd.close();
        DatabaseContext.closeConnection(conn);

        return washers;
    }
}
