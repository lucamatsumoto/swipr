package com.swipr.matcher;

import java.util.ArrayList;

//Temporary subclass of SellQueryListener used for unit testing
class DummyQueryListener implements SellQueryListener {
    public final ArrayList<SellQuery> sellQueryList = new ArrayList<SellQuery>();

    @Override
    public void onMatchFound(SellQuery foundSellQuery) {
        sellQueryList.add(foundSellQuery);
    }

    @Override
    public void onMatchCancelled(SellQuery ignored) {
        return;
    }
}
