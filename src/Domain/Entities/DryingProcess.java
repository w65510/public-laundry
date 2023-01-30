package Domain.Entities;

import java.time.LocalDateTime;

public class DryingProcess
{
    public String TableName = "drying_process";

    public int id;
    public int cost;
    public int washerId;
    public LocalDateTime endDate;
    public String pickupCode;
    public LocalDateTime pickupDate;
}
