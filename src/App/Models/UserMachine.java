package App.Models;

import App.Extensions;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

public class UserMachine
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
            {
                var duration = Duration.between(LocalDateTime.now(), EndDate.toLocalDateTime());
                status = "Zajęta do " + Extensions.DurationToString(duration);
            }
            else
                status = "Pranie do odbioru";
        }

        return String.format("Nazwa: %1$-20s Cena: %2$-10s Ładowność: %3$-8s Status: %4$s",
                Name, Price + "zł/kg", MaxCapacity + "kg", status);
    }
}
