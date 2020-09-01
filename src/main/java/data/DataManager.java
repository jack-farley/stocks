package data;

import java.math.BigDecimal;

public interface DataManager {

    void start();

    void stop();

    BigDecimal getPrice(String ticker);

    void clear();

}
