package controller;

import data.grabber.DataGrabber;

public class ControllerFactory {

    private ControllerFactory() {}

    /**
     * Gets a new console controller using the provided data grabber.
     *
     * @param grabber the grabber
     * @return a controller
     */
    public static Controller getController(DataGrabber grabber) {
        return new Controller(grabber);
    }
}
