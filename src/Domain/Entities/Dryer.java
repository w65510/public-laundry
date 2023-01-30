package Domain.Entities;

import java.time.LocalDateTime;

public class Dryer
{
    public static String TableName = "dryers";

    public int id;
    public String name;
    public int price;
    public int maxCapacity;
    public LocalDateTime buyDate;
    public Boolean isBroken;
}
