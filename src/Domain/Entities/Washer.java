package Domain.Entities;

import java.time.LocalDateTime;

public class Washer
{
    public static String TableName = "washers";

    public int id;
    public String name;
    public int price;
    public int maxCapacity;
    public LocalDateTime buyDate;
    public Boolean isBroken;
}
