package com.swipr.matcher;

import org.junit.*;

public class SellQueryTests {
    int ONE = 1;
    private static SellQuery sq0, sq1, sq2, sq3, sq4, sq5, sq6, sq7;

    @BeforeClass
    public static void setup() {
        sq0 = new SellQuery(1, 0, 10, 500, Query.BPLATE|Query.COVEL, 101);
        sq1 = new SellQuery(1, 0, 10, 500, Query.BPLATE|Query.COVEL, 102);
        sq2 = new SellQuery(0, 0, 10, 500, Query.BPLATE|Query.COVEL, 103);
        sq3 = new SellQuery(1, -5, 10, 500, Query.BPLATE|Query.COVEL, 104);
        sq4 = new SellQuery(1, 0, 5, 500, Query.BPLATE|Query.COVEL, 105);
        sq5 = new SellQuery(1, 0, 10, 400, Query.BPLATE|Query.COVEL, 106);
        sq6 = new SellQuery(1, 0, 10, 500, Query.BPLATE, 107);
        sq7 = new SellQuery(1, 0, 10, 500, Query.BPLATE|Query.COVEL|Query.DE_NEVE, 108);
    }

    @Test
    public void equals_SameSellQuery () {
        //Tests if calling SellQuery.equals on another SellQuery object with the same values for all fields returns true
        Assert.assertTrue(sq1.equals(sq0));
    }

    @Test
    public void equals_DifferentObjects () {
        //Tests if calling SellQuery.equals on a non SellQuery object returns false
        Assert.assertFalse(sq1.equals(ONE));
    }

    @Test
    public void equals_DifferentUserId () {
        //Tests if calling SellQuery.equals on another SellQuery object with a different userId field returns false
        Assert.assertFalse(sq1.equals(sq2));
    }

    @Test
    public void equals_DifferentTimeRangeStart () {
        //Tests if calling SellQuery.equals on another SellQuery object with a different timeRangeStart field returns false
        Assert.assertFalse(sq1.equals(sq3));
    }

    @Test
    public void equals_DifferentTimeRangeEnd () {
        //Tests if calling SellQuery.equals on another SellQuery object with a different timeRangeEnd field returns false
        Assert.assertFalse(sq1.equals(sq4));
    }

    @Test
    public void equals_DifferentPriceCents () {
        //Tests if calling SellQuery.equals on another SellQuery object with a different priceCents field returns false
        Assert.assertFalse(sq1.equals(sq5));
    }

    @Test
    public void equals_DifferentDiningHallBitfield () {
        //Tests if calling SellQuery.equals on another SellQuery object with a different diningHallBitfield field returns false
        Assert.assertFalse(sq1.equals(sq6));
        Assert.assertFalse(sq1.equals(sq7));
    }
}
