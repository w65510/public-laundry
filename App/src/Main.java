public class Main
{
    public static void main(String[] args)
    {
        if (!DatabaseContext.isDatabaseCreated())
            DatabaseContext.createDatabase();

        var conn = DatabaseContext.getConnection();
    }
}
