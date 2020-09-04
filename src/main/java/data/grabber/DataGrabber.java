package data.grabber;

public interface DataGrabber {

    /**
     * Gets the interval at which all market stocks can bea repeatedly updated with this grabber.
     *
     * @return the interval in milliseconds
     */
    int getUpdateInterval();

    /**
     * Used to get information about a specified security.
     *
     * @param ticker the ticker of the security
     * @return information about the security, in the form of a SecurityDetail
     * @throws APICallException if the grabber is unable to get information about the security.
     * This could occur due to a bad ticker or if the api is down.
     */
    SecurityDetail getDetail(String ticker) throws APICallException;

    /**
     *
     * @param search the string used as the search parameter.
     * @return A list of tickers matching the search, sorted from best match to worst.
     */
    String[] searchTicker(String search);

    /**
     * Tests whether the grabber has been set up correctly.
     *
     * @return true if it is set up correctly, false if not
     */
    boolean testSetup();
}
