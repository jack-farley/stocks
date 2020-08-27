package data;

import java.math.BigDecimal;
import java.net.http.HttpResponse;

public class AlphaVantageDataManager implements DataManager {

    final String url = "https://www.alphavantage.co";
    final String endpoint = "query";

    private final String apiKey;

    /**
     * Creates a DataManager using the Alpha Vantage API.
     * @param apiKey The user's api key for alpha vantage.
     */
    public AlphaVantageDataManager(String apiKey){
        this.apiKey = apiKey;
    }

    public BigDecimal getPrice(String ticker) {
        String function = "GLOBAL_QUOTE";
        String datatype = "json";

        String finalURL = String.format("%s/%s?%s=%s&%s=%s&%s=%s&%s=%s", url, endpoint, "function", function, "symbol",
                ticker, "datatype", datatype, "apikey", apiKey);

        HttpResponse<String> response = Unirest.get(finalURL);
        // TODO
        return null;
    }
}
