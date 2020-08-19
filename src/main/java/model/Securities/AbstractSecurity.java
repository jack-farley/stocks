package model.Securities;

public abstract class AbstractSecurity implements Security {

    enum Type {
        Stock,
    }

    private Type type;
    private String symbol;
}
