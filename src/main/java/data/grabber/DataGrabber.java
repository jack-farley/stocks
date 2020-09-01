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
     */
    SecurityDetail getDetail(String ticker);

    /**
     *
     * @param search the string used as the search parameter.
     * @return A list of tickers matching the search, sorted from best match to worst.
     */
    String[] searchTicker(String search);
}
