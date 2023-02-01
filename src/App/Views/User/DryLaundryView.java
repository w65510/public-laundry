package App.Views.User;

import App.Extensions;
import App.InputManager;
import App.Models.Menu;
import App.Models.userMachine;
import App.Views.ApplicationView;
import Domain.Entities.Laundry;
import Infrastructure.Db.QueryManager;

import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Optional;

public class DryLaundryView extends ApplicationView
{
    ArrayList<Laundry> userLaundry = new ArrayList<>();
    userMachine dryer;

    @Override
    public Boolean show() throws SQLException
    {
        dryer = QueryManager.getCurrentUserDryer();

        do
        {
            InputManager.clearConsole();
        } while (manageDryer());

        return false;
    }

    public Boolean manageDryer() {
        var menu = new Menu();

        var laundryWeight = userLaundry.stream().mapToInt(x -> x.weight).sum();

        System.out.println("Cena za 1kg suszenia: " + dryer.Price + "zł");
        System.out.println("Cena Twojego suszenia: " + (laundryWeight * dryer.Price) + "zł");
        var duration = Duration.ofSeconds(laundryWeight * 5);
        System.out.println("Czas trwania suszenia: " + Extensions.DurationToString(duration));
        System.out.println("Pozostała ładowność suszarki: " + (dryer.MaxCapacity - laundryWeight) + "kg\n");

        if (userLaundry.size() == 0)
        {
            System.out.println("Suszarka jest pusta");
        }
        else
        {
            System.out.println("Zawartość suszarki:");
            for (var laundry : userLaundry) {
                System.out.println(laundry);
            }
        }

        if (dryer.MaxCapacity - laundryWeight > 0)
            menu.addItem("Dodaj zawartość do suszenia", () -> addLaundry(), true);

        if (userLaundry.size() > 0)
        {
            menu.addItem("Wyjmij zawartość z suszarki", () -> takeOffLaundry(), true);
            menu.addItem("Rozpocznij suszenie", () -> startDrying(), false);
        }

        menu.addItem("Zrezygnuj z suszenia", () -> { }, false);
        menu.show();

        return menu.handle();
    }

    public void addLaundry() {
        var laundryWeight = userLaundry.stream().mapToInt(x -> x.weight).sum();
        var leftCapacity = dryer.MaxCapacity - laundryWeight;

        var laundry = new Laundry();

        laundry.name = InputManager.getStringPrompt("Podaj nazwę przedmiotu do wysuszenia (1-30 znaków)", Optional.ofNullable(x -> x.length() > 0 && x.length() <= 30));
        laundry.weight = InputManager.getIntPrompt("Podaj wagę przedmiotu (1-" + leftCapacity + ")", Optional.ofNullable(x -> x > 0 && x <= leftCapacity));

        userLaundry.add(laundry);
    }

    public void takeOffLaundry() {
        InputManager.clearConsole();

        System.out.println("Co chcesz wyciągnąć z suszarki?");
        var menu = new Menu();

        for (var i = 0; i < userLaundry.size(); i++) {
            var index = i;
            menu.addItem(userLaundry.get(i).toString(), () -> { userLaundry.remove(index); }, false);
        }

        menu.addItem("Powrót", () -> { }, false);

        menu.show();
        menu.handle();
    }

    public void startDrying()
    {
        if (userLaundry.stream().anyMatch(x -> x.name.equalsIgnoreCase("cegla")))
        {
            System.out.println("Suszarka wyposażona jest w czujnik cegieł. Suszarka nagle wypluła całą swoją zawartość na Ciebie.");
            InputManager.pressEnterToContinue();
            return;
        }

        var pickupCode = QueryManager.createCurrentDryerLaundry(userLaundry);

        System.out.println("Suszarka rozpoczęła proces suszenia");
        System.out.println("Po zakończonym suszeniu, aby odebrać swoje pranie podaj w suszarce kod odbioru: " + pickupCode);
        InputManager.pressEnterToContinue();
    }
}
