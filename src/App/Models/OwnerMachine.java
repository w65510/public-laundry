package App.Models;

import App.Extensions;

import java.time.Duration;
import java.time.LocalDateTime;

public class OwnerMachine extends UserMachine
{
    public Boolean IsBroken;
    public String PickupCode;

    public String getStatus() {
        String status = "Wolna";

        if (EndDate != null) {
            if (Extensions.isInFuture(EndDate))
            {
                var duration = Duration.between(LocalDateTime.now(), EndDate.toLocalDateTime());
                status = "Zajęta do " + Extensions.DurationToString(duration);
            }
            else
                status = "Pranie do odbioru";
        }
        else if (IsBroken)
        {
            status = "Uszkodzona";
        }

        return status;
    }

    public String toString() {
        var status = getStatus();

        return String.format("Nazwa: %1$-20s Cena: %2$-10s Ładowność: %3$-8s Status: %4$s",
                Name, Price + "zł/kg", MaxCapacity + "kg", status);
    }
}
