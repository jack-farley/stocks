package controller;

public class ControllerFactory {

    private ControllerFactory() {}

    /**
     * Gets a new console controller.
     *
     * @return a controller
     */
    public static Controller getConsoleController() {
        String apiKey = "";
        return new Controller(apiKey);
    }

    /**
     * Gets a new gui controller.
     *
     * @return a controller
     */
    public static Controller getGUIController() {
        String apiKey = "";
        return new Controller (apiKey);
    }
}
