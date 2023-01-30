package App;

import App.Views.ApplicationView;

public class ViewManager
{
    public static <TView extends ApplicationView> void showView(Class<TView> clazz)
    {
        try {
            var view = createView(clazz);

            do
            {
                InputManager.clearConsole();
            } while (view.show());
        }
        catch (Exception e)
        {
            System.out.println("Wystąpił błąd:");
            System.out.println(e.getClass().getName()+": "+e.getMessage());
            InputManager.pressEnterToContinue();
        }
    }

    private static <TView extends ApplicationView> TView createView(Class<TView> clazz) {
        try {
            return clazz.getConstructor(null).newInstance();
        }
        catch (Exception e)
        {
            System.out.println("Podczas tworzenia widoku wystąpił błąd.");
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(1);
        }

        return null; // Unreachable code
    }
}
