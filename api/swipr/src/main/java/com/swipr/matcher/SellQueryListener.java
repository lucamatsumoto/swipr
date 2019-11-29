package com.swipr.matcher;

/**
 * A listener interface for notifying users when any changes occur to the SellQuery object that they are listening to. 
 */
public interface SellQueryListener {
    // Called whenever the Matchmaker finds a SellQuery that matches
    // the BuyQuery that was used to register this SellQueryListener
    // with the Matchmaker.
    public void onMatchFound(SellQuery foundSellQuery, boolean update);

    // Called whenever expiredSellQuery is deleted from the Matchmaker,
    // and if expiredSellQuery previously triggered onMatchFound.
    public void onMatchCancelled(SellQuery expiredSellQuery);
}
