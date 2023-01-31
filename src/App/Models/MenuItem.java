package App.Models;

public class MenuItem
{
    public MenuItem(String text, Runnable action, Boolean continueView) {
        this.text = text;
        this.action = action;
        this.continueView = continueView;
    }

    public String text;
    public Runnable action;
    public Boolean continueView;
}
