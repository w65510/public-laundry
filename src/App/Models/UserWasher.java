package App.Models;

import App.Extensions;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class UserWasher
{
    public int Id;
    public String Name;
    public int Price;
    public int MaxCapacity;
    public Timestamp EndDate;

    public String toString() {
        Object status = "Wolna";

        if (EndDate != null) {
            if (Extensions.isInFuture(EndDate))
                status = "Zajęta do " + EndDate;
            else
                status = "Pranie do odbioru";
        }

        return String.format("Nazwa: %1$-20s Cena prania: %2$-8s Ładowność: %3$-8s Status: %4$s",
                Name, Price + "zł", MaxCapacity + "kg", status);
    }
}
