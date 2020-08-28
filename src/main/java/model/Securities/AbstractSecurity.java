package model.Securities;

public abstract class AbstractSecurity implements Security {

    enum SecurityType {
        Stock,
    }
    private SecurityType type;
    private String symbol;
}
