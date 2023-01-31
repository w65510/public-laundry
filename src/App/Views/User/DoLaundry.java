package App.Views.User;

import App.Extensions;
import App.InputManager;
import App.Models.Menu;
import App.Models.UserWasher;
import App.Views.ApplicationView;
import Domain.Entities.Laundry;
import Infrastructure.Db.QueryManager;

import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Optional;

public class DoLaundry extends ApplicationView
{
    ArrayList<Laundry> userLaundry = new ArrayList<>();
    UserWasher washer;

    @Override
    public Boolean show() throws SQLException
    {
        washer = QueryManager.getCurrentUserWasher();

        do
        {
            InputManager.clearConsole();
        } while (manageWasher());

        return false;
    }

    public Boolean manageWasher() {
        var menu = new Menu();

        var laundryWeight = userLaundry.stream().mapToInt(x -> x.weight).sum();

        System.out.println("Cena za 1kg prania: " + washer.Price + "zł");
        System.out.println("Cena Twojego prania: " + (laundryWeight * washer.Price) + "zł");
        var duration = Duration.ofSeconds(laundryWeight * 5);
        System.out.println("Czas trwania prania: " + Extensions.DurationToString(duration));
        System.out.println("Pozostała ładowność pralki: " + (washer.MaxCapacity - laundryWeight) + "kg\n");

        if (userLaundry.size() == 0)
        {
            System.out.println("Pralka jest pusta");
        }
        else
        {
            System.out.println("Zawartość pralki:");
            for (var laundry : userLaundry) {
                System.out.println(laundry);
            }
        }

        if (washer.MaxCapacity - laundryWeight > 0)
            menu.addItem("Dodaj zawartość do prania", () -> addLaundry(), true);

        if (userLaundry.size() > 0)
        {
            menu.addItem("Wyjmij zawartość z pralki", () -> takeOffLaundry(), true);
            menu.addItem("Rozpocznij pranie", () -> startLaundry(), false);
        }

        menu.addItem("Zrezygnuj z prania", () -> { }, false);
        menu.show();

        return menu.handle();
    }

    public void addLaundry() {
        var laundryWeight = userLaundry.stream().mapToInt(x -> x.weight).sum();
        var leftCapacity = washer.MaxCapacity - laundryWeight;

        var laundry = new Laundry();

        laundry.name = InputManager.getStringPrompt("Podaj nazwę przedmiotu do wyprania (1-30 znaków)", Optional.ofNullable(x -> x.length() > 0 && x.length() <= 30));
        laundry.weight = InputManager.getIntPrompt("Podaj wagę przedmiotu (1-" + leftCapacity + ")", Optional.ofNullable(x -> x > 0 && x <= leftCapacity));

        userLaundry.add(laundry);
    }

    public void takeOffLaundry() {
        InputManager.clearConsole();

        System.out.println("Co chcesz wyciągnąć z pralki?");
        var menu = new Menu();

        for (var i = 0; i < userLaundry.size(); i++) {
            var index = i;
            menu.addItem(userLaundry.get(i).toString(), () -> { userLaundry.remove(index); }, false);
        }

        menu.addItem("Powrót", () -> { }, false);

        menu.show();
        menu.handle();
    }

    public void startLaundry()
    {
        if (userLaundry.stream().anyMatch(x -> x.name.equalsIgnoreCase("cegla")))
        {
            QueryManager.setCurrentWasherBroken();
            System.out.println("Niestety uruchomienie pralki z cegłą w środku uszkodziło ją, pralka jest teraz zepsuta.");
            Extensions.openUrlInBrowser("https://www.youtube.com/watch?v=OI8XiH_8UMc");
            InputManager.pressEnterToContinue();
            return;
        }

        var pickupCode = QueryManager.createCurrentWasherLaundry(userLaundry);

        System.out.println("Pralka rozpoczęła proces prania");
        System.out.println("Po zakończonym praniu, aby odebrać swoje pranie podaj w pralce kod odbioru: " + pickupCode);
        InputManager.pressEnterToContinue();
    }
}
