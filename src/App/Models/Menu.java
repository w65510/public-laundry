package App.Models;

import App.InputManager;

import java.util.ArrayList;
import java.util.Optional;

public class Menu
{
    private ArrayList<MenuItem> Items = new ArrayList<MenuItem>();

    public void addItem(String text, Runnable action, Boolean continueView) {
        Items.add(new MenuItem(text, action, continueView));
    }

    public void show() {
        System.out.println();
        for (var i = 0; i < Items.size(); i++) {
            System.out.println((i+1) + ". " + Items.get(i).text);
        }
    }

    public Boolean handle(String text) {
        System.out.println();
        var choice = InputManager.getIntPrompt(text, Optional.ofNullable(x -> x > 0 && x <= Items.size()));

        var item = Items.get(choice-1);
        item.action.run();

        return item.continueView;
    }

    public Boolean handle() {
        return handle("Wybór");
    }
}


