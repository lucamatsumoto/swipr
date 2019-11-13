package com.swipr.matcher;

import java.util.ArrayList;
import org.junit.*;

class SellQueryListListener implements SellQueryListener {
    public final ArrayList<SellQuery> list = new ArrayList<SellQuery>();

    @Override
    public void onMatchFound(SellQuery foundSellQuery) {
        list.add(foundSellQuery);
    }

    @Override
    public void onMatchCancelled(SellQuery ignored) {
        // TODO
    }
}

public class MatchmakerTest {
    @Test
    public void oneBuyerTest() {
        BuyQuery bq = new BuyQuery(0, 0, 10, 1000, Query.BPLATE|Query.COVEL);
        SellQueryListListener listener = new SellQueryListListener();

        // Exactly the same as the buy query, except different user id and
        // different but overlapping dining halls.
        SellQuery sq1 = new SellQuery(1, 0, 10, 1000, Query.BPLATE|Query.FEAST);

        // No dining hall overlap.
        SellQuery sq2 = new SellQuery(2, 0, 10, 1000, Query.DE_NEVE);

        // Non-exact time range overlap matches
        SellQuery sq3 = new SellQuery(3, 5, 15, 1000, Query.BPLATE|Query.COVEL);
        SellQuery sq4 = new SellQuery(4, -3, 15, 1000, Query.BPLATE|Query.COVEL);
        SellQuery sq5 = new SellQuery(5, 9, 10, 1000, Query.BPLATE|Query.COVEL);

        // Price over buyer's price (should not match)
        SellQuery sq6 = new SellQuery(6, 0, 10, 1100, Query.BPLATE);
        SellQuery sq7 = new SellQuery(7, 0, 10, 1001, Query.COVEL);

        // Price under buyer's price (should match)
        SellQuery sq8 = new SellQuery(8, 0, 10, 900, Query.BPLATE);
        SellQuery sq9 = new SellQuery(9, 0, 10, 0, Query.COVEL);

        // Random things that should not match.
        SellQuery sq10 = new SellQuery(10, 16, 200, 800, Query.FEAST|Query.DE_NEVE);
        SellQuery sq11 = new SellQuery(11, 16, 200, 800, Query.FEAST|Query.COVEL);
        SellQuery sq12 = new SellQuery(12, 16, 200, 1200, Query.FEAST|Query.COVEL);

        // Random things that should match.
        SellQuery sq13 = new SellQuery(13, 9, 14, 999, Query.BPLATE|Query.COVEL|Query.DE_NEVE|Query.FEAST);
        SellQuery sq14 = new SellQuery(14, -6, 2, 999, Query.BPLATE);

        Matchmaker m = Matchmaker.getInstance();

        m.updateSellQuery(sq1);
        m.updateSellQuery(sq2);
        m.updateSellQuery(sq3);
        m.updateSellQuery(sq4);
        m.updateSellQuery(sq5);
        m.updateSellQuery(sq6);
        m.updateSellQuery(sq7);

        m.updateBuyQuery(bq, listener);

        m.updateSellQuery(sq8);
        m.updateSellQuery(sq9);
        m.updateSellQuery(sq10);
        m.updateSellQuery(sq11);
        m.updateSellQuery(sq12);
        m.updateSellQuery(sq13);
        m.updateSellQuery(sq14);

        ArrayList<SellQuery> sqList = listener.list;
        assert(sqList.contains(sq1));
        assert(!sqList.contains(sq2));
        assert(sqList.contains(sq3));
        assert(sqList.contains(sq4));
        assert(sqList.contains(sq5));
        assert(!sqList.contains(sq6));
        assert(!sqList.contains(sq7));
        assert(sqList.contains(sq8));
        assert(sqList.contains(sq9));
        assert(!sqList.contains(sq10));
        assert(!sqList.contains(sq11));
        assert(!sqList.contains(sq12));
        assert(sqList.contains(sq13));
        assert(sqList.contains(sq14));
    }
}
