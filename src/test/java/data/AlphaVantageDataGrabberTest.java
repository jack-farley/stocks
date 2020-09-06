package data;

import data.grabber.AlphaVantageDataGrabber;
import data.grabber.DataGrabber;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


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

}
