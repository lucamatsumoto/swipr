package com.swipr.matcher;

import java.util.ArrayList;
import java.util.List;

public class BidQueryListener implements SellQueryListener {


    private List<SellQuery> sellQueryList = new ArrayList<>();

    @Override
    public void onMatchFound(SellQuery sellQuery) {
        // When a match is found, we need to make sure the information is sent to the correct UserID
        // For a buy query, we want to send all of the matched SellQueries back to the buyer
        sellQueryList.add(sellQuery);
        // When a new offer that matches the buy query parameters is added, we need to notify 
    }

    @Override
    public void onMatchCancelled(SellQuery expiredSellQuery) {
        sellQueryList.remove(expiredSellQuery);
    }

    public List<SellQuery> getSellQueryList() {
        return sellQueryList;
    }
}