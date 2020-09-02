package account;

import data.DataManager;

import java.math.BigDecimal;

public interface ReadOnlyPosition {

    /** Returns the ticker of the security of this position. */
    String security();

    /** Returns the quantity of the given security held in this position. */
    int quantity();

    /** Returns the value of this position. */
    BigDecimal value(DataManager data);
}
