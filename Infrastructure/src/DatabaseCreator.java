import java.sql.Connection;

public class DatabaseCreator
{
    public static void createDatabase(Connection conn, String databaseName)
    {
        try
        {
            var cmd = conn.createStatement();
            cmd.execute("CREATE DATABASE " + databaseName + ";");
            cmd.close();
        }
        catch (Exception e)
        {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(1);
        }
    }

    public static void createTables(Connection conn)
    {
        try
        {
            var cmd = conn.createStatement();

            cmd.execute(SqlCollection.CreateTableWasher);
            cmd.execute(SqlCollection.CreateTableWashingProcess);

            cmd.execute(SqlCollection.CreateTableDryer);
            cmd.execute(SqlCollection.CreateTableDryingProcess);

            cmd.execute(SqlCollection.CreateTableWashingLaundry);
            cmd.execute(SqlCollection.CreateTableDryingLaundry);
            cmd.execute(SqlCollection.CreateTableLaundry);

            var sql = "ALTER TABLE washing_process ADD FOREIGN KEY (washer_id) REFERENCES washers (id);\n" +
                    "ALTER TABLE drying_process ADD FOREIGN KEY (dryer_id) REFERENCES dryers (id);\n" +
                    "ALTER TABLE drying_laundry ADD FOREIGN KEY (process_id) REFERENCES drying_process (id);\n" +
                    "ALTER TABLE drying_laundry ADD FOREIGN KEY (laundry_id) REFERENCES laundry (id);\n" +
                    "ALTER TABLE washing_laundry ADD FOREIGN KEY (process_id) REFERENCES washing_process (id);\n" +
                    "ALTER TABLE washing_laundry ADD FOREIGN KEY (laundry_id) REFERENCES laundry (id);";

            cmd.execute(sql);
            cmd.close();
        }
        catch (Exception e)
        {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(1);
        }
    }
}
