package App.Models;

public class UserWasher
{
    public int Id;
    public String Name;
    public int Price;
    public int MaxCapacity;
    public Boolean InProgress;

    public String toString() {
        return String.format("Nazwa: %1$-20s Cena prania: %2$-8s Ładowność: %3$-8s Zajęta: %4$s",
                Name, Price + "zł", MaxCapacity + "kg", InProgress ? "TAK" : "NIE");
    }
}
