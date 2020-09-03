package data.grabber;

public class DataGrabberFactory {

    private DataGrabberFactory() {}

    /**
     * Returns the string name of the data grabber type.
     *
     * @param type the type
     * @return the name
     */
    public static String grabberTypeName(DataGrabberType type) {
        switch (type) {
            case AlphaVantage:
                return "AlphaVantage";
            default:
                return null;
        }
    }

    /**
     * Creates a new data grabber using the AlphaVantage api.
     *
     * @param apiKey the api key
     * @return the data grabber
     */
    public static DataGrabber newAlphaVantageGrabber(String apiKey) {
        return new AlphaVantageDataGrabber(apiKey);
    }
}
