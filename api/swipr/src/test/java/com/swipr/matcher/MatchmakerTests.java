package com.swipr.matcher;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class MatchmakerTests {
    private static Matchmaker m;
    private static DummyQueryListener l1;
    private static ArrayList<SellQuery> sqList1;
    private static BuyQuery bq1;
    private static SellQuery sq1, sq2, sq3, sq4, sq5, sq6, sq7, sq8, sq9, sq10, sq11, sq12;

    @BeforeClass
    public static void setup() {
        m = Matchmaker.getInstance();

        l1 = new DummyQueryListener();

        bq1 = new BuyQuery(0, 0, 10, 500, Query.BPLATE|Query.COVEL);

        sq1 = new SellQuery(1, 0, 10, 500, Query.BPLATE|Query.COVEL);
        sq2 = new SellQuery(2, 0, 10, 499, Query.BPLATE|Query.COVEL);
        sq3 = new SellQuery(3, 0, 10, 501, Query.BPLATE|Query.COVEL);
        sq4 = new SellQuery(4, 1, 9, 500, Query.BPLATE|Query.COVEL);
        sq5 = new SellQuery(5, -1, 9, 500, Query.BPLATE|Query.COVEL);
        sq6 = new SellQuery(6, -1, 11, 500, Query.BPLATE|Query.COVEL);
        sq7 = new SellQuery(7, 1, 11, 500, Query.BPLATE|Query.COVEL);
        sq8 = new SellQuery(8, 11, 20, 500, Query.BPLATE|Query.COVEL);
        sq9 = new SellQuery(9, -10, -1, 500, Query.BPLATE|Query.COVEL);
        sq10 = new SellQuery(10, 0, 10, 500, Query.COVEL);
        sq11 = new SellQuery(11, 0, 10, 500, Query.BPLATE|Query.COVEL|Query.DE_NEVE);
        sq12 = new SellQuery(12, 0, 10, 500, Query.DE_NEVE|Query.FEAST);

        m.updateBuyQuery(bq1, l1);

        m.updateSellQuery(sq1);
        m.updateSellQuery(sq2);
        m.updateSellQuery(sq3);
        m.updateSellQuery(sq4);
        m.updateSellQuery(sq5);
        m.updateSellQuery(sq6);
        m.updateSellQuery(sq7);
        m.updateSellQuery(sq8);
        m.updateSellQuery(sq9);
        m.updateSellQuery(sq10);
        m.updateSellQuery(sq11);
        m.updateSellQuery(sq12);

        sqList1 = l1.sellQueryList;
    }

    @Test
    public void exactSameQuery_Test() {
        //Checks if a SellQuery with the exact same parameters as a BuyQuery gets added to its sellQueryList
        Assert.assertTrue(sqList1.contains(sq1));
    }

    @Test
    public void lowerPriceQuery_Test() {
        //Checks if a SellQuery with a lower price than the BuyQuery gets added to its sellQueryList
        Assert.assertTrue(sqList1.contains(sq2));
    }

    @Test
    public void higherPriceQuery_Test() {
        //Checks if a SellQuery with a higher price than the BuyQuery gets ignored
        Assert.assertFalse(sqList1.contains(sq3));
    }

    @Test
    public void overlappingTimerange_Test() {
        //Checks if a SellQuery and a BuyQuery with overlapping time ranges gets added to its sellQueryList
        Assert.assertTrue(sqList1.contains(sq4));
        Assert.assertTrue(sqList1.contains(sq5));
        Assert.assertTrue(sqList1.contains(sq6));
        Assert.assertTrue(sqList1.contains(sq7));
    }

    @Test
    public void noOverlappingTimerange_Test() {
        //Checks if a SellQuery and a BuyQuery with no overlapping time ranges gets ignored
        Assert.assertFalse(sqList1.contains(sq8));
        Assert.assertFalse(sqList1.contains(sq9));
    }

    @Test
    public void OverlappingDiningHalls_Test() {
        //Checks if a SellQuery and a BuyQuery with no overlapping dining halls gets added to its sellQueryList
        Assert.assertTrue(sqList1.contains(sq10));
        Assert.assertTrue(sqList1.contains(sq11));
    }

    @Test
    public void noOverlappingDiningHalls_Test() {
        //Checks if a SellQuery and a BuyQuery with no overlapping dining halls gets ignored
        Assert.assertFalse(sqList1.contains(sq12));
    }
}
