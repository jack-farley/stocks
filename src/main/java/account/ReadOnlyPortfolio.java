package account;

import data.DataManager;

import java.math.BigDecimal;
import java.util.Collection;

public interface ReadOnlyPortfolio {

    /** Returns the name of the portfolio. */
    String name();

    /** Returns the value of this portfolio, or null if the value cannot be found. */
    BigDecimal value(DataManager data);

    /**
     * Gets the list of positions in this portfolio.
     *
     * @return the positions.
     */
    Collection<? extends ReadOnlyPosition> positions();

    /**
     * Gets the position in this portfolio associated with the specified security.
     *
     * @param ticker The security's ticker.
     * @return The position associated with that security, or null if no such position exists.
     */
    ReadOnlyPosition position(String ticker);

}
