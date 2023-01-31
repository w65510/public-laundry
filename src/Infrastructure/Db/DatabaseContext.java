package Infrastructure.Db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseContext
{
    private static String DATABASE_NAME = "wash_house";

    public static Boolean isDatabaseCreated()
    {
        var conn = getConnection("postgres");
        var isCreated = false;

        try
        {
            var cmd = conn.createStatement();
            var res = cmd.executeQuery("select exists(" +
                    "SELECT datname FROM pg_catalog.pg_database WHERE lower(datname) = lower('" + DATABASE_NAME + "')" +
                    ");");

            res.next();
            isCreated = res.getBoolean(1);
            cmd.close();
        }
        catch (Exception e)
        {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(1);
        }
        finally
        {
            closeConnection(conn);
        }

        return isCreated;
    }

    public static void createDatabase()
    {
        var conn = getConnection("postgres", false);

        DatabaseCreator.createDatabase(conn, DATABASE_NAME);
        saveChanges(conn, true);

        conn = getConnection();
        DatabaseCreator.createTables(conn);
        saveChanges(conn);
    }

    public static void saveChanges(Connection conn)
    {
        saveChanges(conn, false);
    }

    public static void saveChanges(Connection conn, Boolean close)
    {
        try
        {
            if (!conn.getAutoCommit())
                conn.commit();

            if (close) closeConnection(conn);
        }
        catch (Exception e)
        {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(1);
        }
    }

    public static void RollBack(Connection conn)
    {
        RollBack(conn, false);
    }

    public static void RollBack(Connection conn, Boolean close)
    {
        try
        {
            if (!conn.getAutoCommit())
                conn.rollback();

            if (close) closeConnection(conn);
        }
        catch (Exception e)
        {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(1);
        }
    }

    public static Connection getConnection()
    {
        return getConnection("wash_house");
    }

    public static void closeConnection(Connection conn)
    {
        try
        {
            conn.close();
        }
        catch (Exception e)
        {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(1);
        }
    }

    private static Connection getConnection(String databaseName, Boolean transaction)
    {
        Connection connection = null;

        try
        {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + databaseName,
                    "postgres", "Aaa123456");

            connection.setAutoCommit(!transaction);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(1);
        }

        return connection;
    }

    private static Connection getConnection(String databaseName)
    {
        return getConnection(databaseName, true);
    }
}
