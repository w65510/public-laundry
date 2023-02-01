package App.Views;

import App.InputManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.Optional;

public abstract class ApplicationView
{
    public abstract Boolean show() throws SQLException;
}
