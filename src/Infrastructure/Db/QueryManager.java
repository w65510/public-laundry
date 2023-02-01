package Infrastructure.Db;

import App.Extensions;
import App.GlobalVariables;
import App.Models.OwnerMachine;
import App.Models.OwnerMachineStatistics;
import App.Models.UserMachine;
import Domain.Entities.*;

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

    public static int getDryersCount(String where) throws SQLException
    {
        var sql = "SELECT COUNT(*) FROM " + Dryer.TableName + " ";

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

    public static ArrayList<Laundry> getCurrentWasherLaundry() {
        try {
            var sql = """
                         SELECT name, weight FROM %1$s l
                         INNER JOIN washing_laundry wl
                         ON wl.laundry_id = l.id
                         WHERE wl.process_id =
                         (
                            SELECT id FROM %2$s
                            WHERE washer_id = %3$d
                            AND pickup_date is null
                         );
                    """.formatted(Laundry.TableName, WashingProcess.TableName, GlobalVariables.SelectedWasher);

            var conn = DatabaseContext.getConnection();
            Statement cmd = conn.createStatement();
            var res = cmd.executeQuery(sql);

            var laundry = new ArrayList<Laundry>();
            while (res.next())
            {
                var laund = new Laundry();
                laund.name = res.getString(1);
                laund.weight = res.getInt(2);
                laundry.add(laund);
            }
            cmd.close();

            DatabaseContext.closeConnection(conn);

            return laundry;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Laundry> getCurrentDryerLaundry() {
        try {
            var sql = """
                         SELECT name, weight FROM %1$s l
                         INNER JOIN drying_laundry dl
                         ON dl.laundry_id = l.id
                         WHERE dl.process_id =
                         (
                            SELECT id FROM %2$s
                            WHERE dryer_id = %3$d
                            AND pickup_date is null
                         );
                    """.formatted(Laundry.TableName, DryingProcess.TableName, GlobalVariables.SelectedDryer);

            var conn = DatabaseContext.getConnection();
            Statement cmd = conn.createStatement();
            var res = cmd.executeQuery(sql);

            var laundry = new ArrayList<Laundry>();
            while (res.next())
            {
                var laund = new Laundry();
                laund.name = res.getString(1);
                laund.weight = res.getInt(2);
                laundry.add(laund);
            }
            cmd.close();

            DatabaseContext.closeConnection(conn);

            return laundry;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static UserMachine getCurrentUserWasher() throws SQLException
    {
        var washers = QueryManager.getUserWashers().stream().filter(x -> x.Id == GlobalVariables.SelectedWasher).collect(Collectors.toList());

        if (washers.size() == 0)
            return null;

        return washers.get(0);
    }

    public static OwnerMachine getCurrentOwnerWasher() throws SQLException
    {
        var washers = QueryManager.getOwnerWashers().stream().filter(x -> x.Id == GlobalVariables.SelectedWasher).collect(Collectors.toList());

        if (washers.size() == 0)
            return null;

        return washers.get(0);
    }

    public static UserMachine getCurrentUserDryer() throws SQLException
    {
        var dryers = QueryManager.getUserDryers().stream().filter(x -> x.Id == GlobalVariables.SelectedDryer).collect(Collectors.toList());

        if (dryers.size() == 0)
            return null;

        return dryers.get(0);
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

    public static ArrayList<UserMachine> getUserWashers() throws SQLException
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

        var washers = new ArrayList<UserMachine>();

        while (res.next()) {
            var washer = new UserMachine();

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

    public static ArrayList<OwnerMachine> getOwnerWashers() throws SQLException
    {
        var sql = """
                SELECT w.id, name, price, maxCapacity, is_broken,
                (
                    SELECT end_date FROM %2$s
                    WHERE washer_id = w.id
                    AND pickup_date is null
                ) as busy,
                (
                    SELECT pickup_code FROM %2$s
                    WHERE washer_id = w.id
                    AND pickup_date is null
                ) as pickup
                FROM %1$s w
                LEFT JOIN washing_process wp
                ON w.id = wp.washer_id
                GROUP BY w.id, name, price, maxCapacity;
                """.formatted(Washer.TableName, WashingProcess.TableName);

        var conn = DatabaseContext.getConnection();
        var cmd = conn.createStatement();
        var res = cmd.executeQuery(sql);

        var washers = new ArrayList<OwnerMachine>();

        while (res.next()) {
            var washer = new OwnerMachine();

            washer.Id = res.getInt(1);
            washer.Name = res.getString(2);
            washer.Price = res.getInt(3);
            washer.MaxCapacity = res.getInt(4);
            washer.IsBroken = res.getBoolean(5);
            washer.EndDate = res.getTimestamp(6);
            washer.PickupCode = res.getString(7);

            washers.add(washer);
        }

        cmd.close();
        DatabaseContext.closeConnection(conn);

        return washers;
    }

    public static ArrayList<UserMachine> getUserDryers() throws SQLException
    {
        var sql = """
                SELECT w.id, name, price, maxCapacity,
                (
                    SELECT end_date FROM %2$s
                    WHERE dryer_id = w.id
                    AND pickup_date is null
                ) as busy
                FROM %1$s w
                LEFT JOIN drying_process wp
                ON w.id = wp.dryer_id
                WHERE w.is_broken = false
                GROUP BY w.id, name, price, maxCapacity;
                """.formatted(Dryer.TableName, DryingProcess.TableName);

        var conn = DatabaseContext.getConnection();
        var cmd = conn.createStatement();
        var res = cmd.executeQuery(sql);

        var dryers = new ArrayList<UserMachine>();

        while (res.next()) {
            var dryer = new UserMachine();

            dryer.Id = res.getInt(1);
            dryer.Name = res.getString(2);
            dryer.Price = res.getInt(3);
            dryer.MaxCapacity = res.getInt(4);
            dryer.EndDate = res.getTimestamp(5);

            dryers.add(dryer);
        }

        cmd.close();
        DatabaseContext.closeConnection(conn);

        return dryers;
    }



    public static Boolean pickupLaundry(String pickupCode)
    {
        try {
            var sql = """
                        UPDATE %1$s
                        SET pickup_date = NOW()
                        WHERE pickup_code ilike '%2$s'
                        AND pickup_date is null
                        RETURNING id;
                    """.formatted(WashingProcess.TableName, pickupCode);

            var conn = DatabaseContext.getConnection();
            var cmd = conn.createStatement();
            var res = cmd.executeQuery(sql);

            var isValid = res.next();

            cmd.close();
            DatabaseContext.saveChanges(conn, true);

            return isValid;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String createCurrentDryerLaundry(ArrayList<Laundry> userLaundry)
    {
        try
        {
            // Create process
            var dryer = getCurrentUserDryer();

            var laundryWeight = userLaundry.stream().mapToInt(x -> x.weight).sum();
            var dryingCost = laundryWeight * dryer.Price;
            var endDate = LocalDateTime.now().plus(Duration.ofSeconds(laundryWeight * 5));
            var pickupCode = Extensions.generatePickupCode();

            var sql = """
                INSERT INTO %1$s (cost, dryer_id, end_date, pickup_code)
                VALUES (%2$d, %3$d, '%4$s', '%5$s')
                RETURNING id;
                """.formatted(DryingProcess.TableName, dryingCost, dryer.Id, endDate, pickupCode);

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
            builder.append("INSERT INTO %1$s (process_id, laundry_id) VALUES\n".formatted("drying_laundry"));

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

    public static Boolean pickupDriedLaundry(String pickupCode)
    {
        try {
            var sql = """
                        UPDATE %1$s
                        SET pickup_date = NOW()
                        WHERE pickup_code ilike '%2$s'
                        AND pickup_date is null
                        RETURNING id;
                    """.formatted(DryingProcess.TableName, pickupCode);

            var conn = DatabaseContext.getConnection();
            var cmd = conn.createStatement();
            var res = cmd.executeQuery(sql);

            var isValid = res.next();

            cmd.close();
            DatabaseContext.saveChanges(conn, true);

            return isValid;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteWasher(Integer id)
    {
        try {
            var sql = """
                        DELETE FROM %1$s
                        WHERE id = %2$d;
                    """.formatted(Washer.TableName, id);

            var conn = DatabaseContext.getConnection();
            var cmd = conn.createStatement();
            cmd.execute(sql);

            cmd.close();
            DatabaseContext.saveChanges(conn, true);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void fixWasher(int id)
    {
        try {
            var sql = """
                        UPDATE %1$s
                        SET is_broken = false 
                        WHERE id = %2$d;
                    """.formatted(Washer.TableName, id);

            var conn = DatabaseContext.getConnection();
            var cmd = conn.createStatement();
            cmd.execute(sql);

            cmd.close();
            DatabaseContext.saveChanges(conn, true);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void stopCurrentWasher()
    {
        var endDate = LocalDateTime.now().minus(Duration.ofSeconds(5));

        try {
            var sql = """
                        UPDATE %1$s
                        SET end_date = '%2$s'
                        WHERE washer_id = %3$d
                        AND pickup_date IS NULL;
                    """.formatted(WashingProcess.TableName, endDate, GlobalVariables.SelectedWasher);

            var conn = DatabaseContext.getConnection();
            var cmd = conn.createStatement();
            cmd.execute(sql);

            cmd.close();
            DatabaseContext.saveChanges(conn, true);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static OwnerMachineStatistics getCurrentOwnerWasherStatistics()
    {
        try {
            var washer = getCurrentOwnerWasher();

            var sql = """
                        SELECT COUNT(*), SUM(cost) FROM %1$s
                        WHERE washer_id = %2$d
                        GROUP BY washer_id;
                    """.formatted(WashingProcess.TableName, GlobalVariables.SelectedWasher);

            var conn = DatabaseContext.getConnection();
            var cmd = conn.createStatement();
            var res = cmd.executeQuery(sql);

            var statistics = new OwnerMachineStatistics();
            statistics.Id = washer.Id;
            statistics.IsBroken = washer.IsBroken;
            statistics.Name = washer.Name;
            statistics.EndDate = washer.EndDate;
            statistics.MaxCapacity = washer.MaxCapacity;
            statistics.PickupCode = washer.PickupCode;
            statistics.Price = washer.Price;

            if (res.next())
            {
                statistics.ProcessCount = res.getInt(1);
                statistics.EarnSum = res.getInt(2);
            }

            cmd.close();
            DatabaseContext.closeConnection(conn);

            return statistics;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
