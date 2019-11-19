package com.swipr.matcher;

import java.util.ArrayList;
import org.junit.*;

public class MatchmakerTests {
    private static Matchmaker m;

    private static DummyQueryListener listener1;

    private static ArrayList<SellQuery> sqList1;

    private static BuyQuery bq1 = new BuyQuery(0, 0, 10, 500, Query.BPLATE|Query.COVEL);

    private static SellQuery sq1 = new SellQuery(1, 0, 10, 500, Query.BPLATE|Query.COVEL, 101);
    private static SellQuery sq2 = new SellQuery(2, 0, 10, 499, Query.BPLATE|Query.COVEL, 102);
    private static SellQuery sq3 = new SellQuery(3, 0, 10, 501, Query.BPLATE|Query.COVEL, 103);
    private static SellQuery sq4 = new SellQuery(4, 1, 9, 500, Query.BPLATE|Query.COVEL, 104);
    private static SellQuery sq5 = new SellQuery(5, -1, 9, 500, Query.BPLATE|Query.COVEL, 105);
    private static SellQuery sq6 = new SellQuery(6, -1, 11, 500, Query.BPLATE|Query.COVEL, 106);
    private static SellQuery sq7 = new SellQuery(7, 1, 11, 500, Query.BPLATE|Query.COVEL, 107);
    private static SellQuery sq8 = new SellQuery(8, 11, 20, 500, Query.BPLATE|Query.COVEL, 108);
    private static SellQuery sq9 = new SellQuery(9, -10, -1, 500, Query.BPLATE|Query.COVEL, 109);
    private static SellQuery sq10 = new SellQuery(10, 0, 10, 500, Query.COVEL, 110);
    private static SellQuery sq11 = new SellQuery(11, 0, 10, 500, Query.BPLATE|Query.COVEL|Query.DE_NEVE, 111);
    private static SellQuery sq12 = new SellQuery(12, 0, 10, 500, Query.DE_NEVE|Query.FEAST, 112);

    @BeforeClass
    public static void setup () {
        m = Matchmaker.getInstance();
        listener1 = new DummyQueryListener();

        m.updateBuyQuery(bq1, listener1);

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

        sqList1 = listener1.sellQueryList;
    }

    @Test
    public void exactSameQuery () {
        //Checks if a SellQuery with the exact same parameters as a BuyQuery gets added to its sellQueryList
        Assert.assertTrue(sqList1.contains(sq1));
    }

    @Test
    public void lowerPriceQuery () {
        //Checks if a SellQuery with a lower price than the BuyQuery gets added to its sellQueryList
        Assert.assertTrue(sqList1.contains(sq2));
    }

    @Test
    public void higherPriceQuery () {
        //Checks if a SellQuery with a higher price than the BuyQuery gets ignored
        Assert.assertFalse(sqList1.contains(sq3));
    }

    @Test
    public void overlappingTimerange () {
        //Checks if a SellQuery and a BuyQuery with overlapping time ranges gets added to its sellQueryList
        Assert.assertTrue(sqList1.contains(sq4));
        Assert.assertTrue(sqList1.contains(sq5));
        Assert.assertTrue(sqList1.contains(sq6));
        Assert.assertTrue(sqList1.contains(sq7));
    }

    @Test
    public void noOverlappingTimerange () {
        //Checks if a SellQuery and a BuyQuery with no overlapping time ranges gets ignored
        Assert.assertFalse(sqList1.contains(sq8));
        Assert.assertFalse(sqList1.contains(sq9));
    }

    @Test
    public void OverlappingDiningHalls () {
        //Checks if a SellQuery and a BuyQuery with no overlapping dining halls gets added to its sellQueryList
        Assert.assertTrue(sqList1.contains(sq10));
        Assert.assertTrue(sqList1.contains(sq11));
    }

    @Test
    public void noOverlappingDiningHalls () {
        //Checks if a SellQuery and a BuyQuery with no overlapping dining halls gets ignored
        Assert.assertFalse(sqList1.contains(sq12));
    }
}
