package data;

import java.math.BigDecimal;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

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

    @Override
    public BigDecimal getPrice(String ticker) {
        String function = "GLOBAL_QUOTE";
        String datatype = "json";

        String URLEndpoint = String.format("%s/%s", url, endpoint);

        HttpResponse <JsonNode> response = Unirest.get(URLEndpoint)
                .queryString("function", function)
                .queryString("symbol", ticker)
                .queryString("datatype", datatype)
                .queryString("apiKey", this.apiKey)
                .asJson();

        JSONObject stockInfo = response.getBody().getObject();
        String priceString = stockInfo.getString("05. price");

        return new BigDecimal (priceString);
    }

    @Override
    public String[] searchTicker(String search) {
        String function = "SYMBOL_SEARCH";
        String datatype = "json";

        String URLEndpoint = String.format("%s/%s", url, endpoint);

        HttpResponse <JsonNode> response = Unirest.get(URLEndpoint)
                .queryString("function", function)
                .queryString("keywords", search)
                .queryString("datatype", datatype)
                .queryString("apiKey", this.apiKey)
                .asJson();

        JSONArray stocks = response.getBody().getArray();
        int num = stocks.length();
        String[] tickers = new String[num];

        for (int i = 0; i < num; i ++) {
            tickers[i] = stocks.getJSONObject(i).getString("symbol");
        }
        return tickers;
    }
}
