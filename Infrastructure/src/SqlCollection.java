class SqlCollection
{
    public static String CreateTableWasher =
            "CREATE TABLE washers (\n" +
                    "  id int PRIMARY KEY,\n" +
                    "  name text,\n" +
                    "  price int,\n" +
                    "  maxCapacity int,\n" +
                    "  buy_date datetime,\n" +
                    "  is_broken bool\n" +
                    ");";

    public static String CreateTableDryer =
            "CREATE TABLE dryers (\n" +
                    "  id int PRIMARY KEY,\n" +
                    "  name text,\n" +
                    "  price int,\n" +
                    "  maxCapacity int,\n" +
                    "  buy_date datetime,\n" +
                    "  is_broken bool\n" +
                    ");";

    public static String CreateTableWashingProcess =
            "CREATE TABLE washing_process (\n" +
                    "  id int PRIMARY KEY,\n" +
                    "  cost int,\n" +
                    "  washer_id int,\n" +
                    "  end_date datetime,\n" +
                    "  pickup_code string,\n" +
                    "  pickup_date datetime\n" +
                    ");";

    public static String CreateTableDryingProcess =
            "CREATE TABLE drying_process (\n" +
                    "  id int PRIMARY KEY,\n" +
                    "  cost int,\n" +
                    "  dryer_id int,\n" +
                    "  end_date datetime,\n" +
                    "  pickup_code string,\n" +
                    "  pickup_date datetime\n" +
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
                    "  id int PRIMARY KEY,\n" +
                    "  name text,\n" +
                    "  weight int\n" +
                    ");";
}

