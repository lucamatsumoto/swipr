package com.swipr.matcher;

public interface SellQueryListener {
    // Called whenever the Matchmaker finds a SellQuery that matches
    // the BuyQuery that was used to register this SellQueryListener
    // with the Matchmaker.
    public void onMatchFound(SellQuery foundSellQuery);

    // Called whenever expiredSellQuery is deleted from the Matchmaker,
    // and if expiredSellQuery previously triggered onMatchFound.
    public void onMatchCancelled(SellQuery expiredSellQuery);
}
