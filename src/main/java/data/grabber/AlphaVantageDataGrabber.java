package data.grabber;

import data.market.securities.SecurityType;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;

import java.math.BigDecimal;

public class AlphaVantageDataGrabber implements DataGrabber {

    final String url = "https://www.alphavantage.co";
    final String endpoint = "query";
    final int updateInterval = 5000;
    final String testTicker = "AAPL";

    private final String apiKey;

    /**
     * Creates a DataManager using the Alpha Vantage API.
     * @param apiKey The user's api key for alpha vantage.
     */
    public AlphaVantageDataGrabber(String apiKey){
        this.apiKey = apiKey;
    }

    @Override
    public int getUpdateInterval() {
        return this.updateInterval;
    }

    @Override
    public SecurityDetail getDetail(String ticker) throws APICallException {
        String function = "GLOBAL_QUOTE";
        String datatype = "json";

        String URLEndpoint = String.format("%s/%s", url, endpoint);

        HttpResponse <JsonNode> response = Unirest.get(URLEndpoint)
                .queryString("function", function)
                .queryString("symbol", ticker)
                .queryString("datatype", datatype)
                .queryString("apikey", this.apiKey)
                .asJson();

        try {
            JSONObject stockInfo = response.getBody().getObject().getJSONObject("Global Quote");
            String priceString = stockInfo.getString("05. price");
            if (priceString == null) {
                throw new APICallException("Unable to get stock detail.");
            }
            return new SecurityDetail (ticker, new BigDecimal (priceString), SecurityType.Stock);
        } catch (JSONException e) {
            throw new APICallException("Unable to get stock detail.");
        }
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
                .queryString("apikey", this.apiKey)
                .asJson();

        JSONArray stocks = response.getBody().getArray();
        int num = stocks.length();
        String[] tickers = new String[num];

        for (int i = 0; i < num; i ++) {
            tickers[i] = stocks.getJSONObject(i).getString("symbol");
        }
        return tickers;
    }

    @Override
    public boolean testSetup() {
        try {
            SecurityDetail appleDetail = this.getDetail(this.testTicker);
            BigDecimal price = appleDetail.getPrice();
            return true;
        } catch (APICallException e) {
            return false;
        }
    }
}
