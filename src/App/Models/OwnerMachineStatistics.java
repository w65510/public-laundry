package App.Models;

import App.Extensions;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class OwnerMachineStatistics extends OwnerMachine
{
    public int ProcessCount;
    public int EarnSum;

    public String toString() {
        var builder = new StringBuilder();
        builder.append("Nazwa:                   " + Name + "\n");
        builder.append("Data zakupu:             " + Extensions.TimeStampToString(BuyDate) + "\n");
        builder.append("Cena za kg:              " + Price + "zł\n");
        builder.append("Ładowność:               " + MaxCapacity + "kg\n");
        builder.append("Status:                  " + getStatus() + "\n");

        if (EndDate != null)
            builder.append("Kod odbioru:             " + PickupCode + "\n");

        builder.append("Ilość cykli:             " + ProcessCount + "\n");
        builder.append("Zysk na urządzeniu:      " + EarnSum + "zł");

        return builder.toString();
    }
}
