package data;

import java.math.BigDecimal;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.JsonNode;

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

        String URLEndpoint = String.format("%s/%s", url, endpoint);

        HttpResponse <JsonNode> response = Unirest.get(URLEndpoint)
                .queryString("function", function)
                .queryString("symbol", ticker)
                .queryString("datatype", datatype)
                .queryString("apiKey", apiKey)
                .asJson();

        String priceString = response.getBody().getObject().getString("05. price");

        return new BigDecimal (priceString);
    }
}
