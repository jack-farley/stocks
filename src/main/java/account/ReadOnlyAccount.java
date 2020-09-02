package account;

import java.util.Collection;

public interface ReadOnlyAccount {

    /** Returns a collection of the portfolios in the account. */
    Collection<? extends ReadOnlyPortfolio> getPortfolios();

    /** Returns the portfolio in the account with the given name. */
    ReadOnlyPortfolio getPortfolio(String name);
}
