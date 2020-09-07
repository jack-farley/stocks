package data;

import data.grabber.AlphaVantageDataGrabber;
import data.grabber.DataGrabber;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.math.BigDecimal;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataManagerTest {

    String apiKey = "PQA3TAHL96ZVUCKL";
    DataGrabber grabber = new AlphaVantageDataGrabber(this.apiKey);
    DataManager manager = new StockDataManager(grabber);

    @Test
    @Order(0)
    public void test_valid_get_price() {
        BigDecimal applePrice = this.manager.getPrice("AAPL");
        assert (applePrice != null);
        assert (applePrice.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @Order(1)
    public void test_invalid_get_price() {
        BigDecimal fakeStockPrice = this.manager.getPrice("KWORG");
        assert (fakeStockPrice == null);
    }
}
