package data;

import data.grabber.APICallException;
import data.grabber.AlphaVantageDataGrabber;
import data.grabber.DataGrabber;
import data.grabber.SecurityDetail;
import data.market.securities.SecurityType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.math.BigDecimal;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AlphaVantageDataGrabberTest {

    String apiKey = "PQA3TAHL96ZVUCKL";
    DataGrabber grabber = new AlphaVantageDataGrabber(this.apiKey);

    @Test
    @Order(0)
    public void test_update_interval() {
        int updateInterval = grabber.getUpdateInterval();

        assert (updateInterval == 5000);
    }

    @Test
    @Order(1)
    public void test_setup() {
        assert (grabber.testSetup());
    }

    @Test
    @Order(2)
    public void test_get_detail() {
        SecurityDetail appleDetail = null;
        try {
            appleDetail = grabber.getDetail("AAPL");
        } catch (APICallException e) {
            assert (false);
        }

        assert (appleDetail.getTicker().equals("AAPL"));
        BigDecimal price = appleDetail.getPrice();
        assert (price.compareTo(BigDecimal.ZERO) > 0);
        assert (appleDetail.getType().equals(SecurityType.Stock));
    }

}
