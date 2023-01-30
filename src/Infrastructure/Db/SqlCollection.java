package Infrastructure.Db;

class SqlCollection
{
    public static String CreateTableWasher =
            "CREATE TABLE washers (\n" +
                    "  id SERIAL PRIMARY KEY,\n" +
                    "  name text,\n" +
                    "  price int,\n" +
                    "  maxCapacity int,\n" +
                    "  buy_date timestamp,\n" +
                    "  is_broken bool\n" +
                    ");";

    public static String CreateTableDryer =
            "CREATE TABLE dryers (\n" +
                    "  id SERIAL PRIMARY KEY,\n" +
                    "  name text,\n" +
                    "  price int,\n" +
                    "  maxCapacity int,\n" +
                    "  buy_date timestamp,\n" +
                    "  is_broken bool\n" +
                    ");";

    public static String CreateTableWashingProcess =
            "CREATE TABLE washing_process (\n" +
                    "  id SERIAL PRIMARY KEY,\n" +
                    "  cost int,\n" +
                    "  washer_id int,\n" +
                    "  end_date timestamp,\n" +
                    "  pickup_code text,\n" +
                    "  pickup_date timestamp\n" +
                    ");";

    public static String CreateTableDryingProcess =
            "CREATE TABLE drying_process (\n" +
                    "  id SERIAL PRIMARY KEY,\n" +
                    "  cost int,\n" +
                    "  dryer_id int,\n" +
                    "  end_date timestamp,\n" +
                    "  pickup_code text,\n" +
                    "  pickup_date timestamp\n" +
                    ");";

    public static String CreateTableWashingLaundry =
            "CREATE TABLE washing_laundry (\n" +
                    "  process_id int,\n" +
                    "  laundry_id int,\n" +
                    "  PRIMARY KEY (process_id, laundry_id)\n" +
                    ");";

    public static String CreateTableDryingLaundry =
            "CREATE TABLE drying_laundry (\n" +
                    "  process_id int,\n" +
                    "  laundry_id int,\n" +
                    "  PRIMARY KEY (process_id, laundry_id)\n" +
                    ");";

    public static String CreateTableLaundry =
            "CREATE TABLE laundry (\n" +
                    "  id SERIAL PRIMARY KEY,\n" +
                    "  name text,\n" +
                    "  weight int\n" +
                    ");";
}

