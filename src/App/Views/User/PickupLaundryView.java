package App.Views.User;

import App.GlobalVariables;
import App.InputManager;
import App.Views.ApplicationView;
import Infrastructure.Db.QueryManager;

import java.sql.SQLException;

public class PickupLaundryView extends ApplicationView
{
    @Override
    public Boolean show() throws SQLException
    {
        if (GlobalVariables.IsOwner) {
            var washer = QueryManager.getCurrentOwnerWasher();
            pickupLaundry(washer.PickupCode);

            System.out.println("Pranie zostało odebrane");
            InputManager.pressEnterToContinue();

            return false;
        }

        var pickupCode = InputManager.getStringPrompt("Aby odebrać pranie podaj kod odbioru");
        Boolean isCodeValid = pickupLaundry(pickupCode);

        if (!isCodeValid) {
            System.out.println("Kod odbioru jest niepoprawny. Jeżeli go nie pamiętasz poproś właściciela o pomoc.");
        }
        else {
            System.out.println("Kod odbioru jest poprawny. Twoje pranie zostało odebrane.");
        }

        InputManager.pressEnterToContinue();

        return false;
    }

    private Boolean pickupLaundry(String pickupCode) {
        if (GlobalVariables.SelectedWasher != -1)
            return QueryManager.pickupLaundry(pickupCode);
        else if (GlobalVariables.SelectedDryer != -1)
            return QueryManager.pickupDriedLaundry(pickupCode);

        return false;
    }
}
