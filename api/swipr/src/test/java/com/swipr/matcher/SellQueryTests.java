package com.swipr.matcher;

import org.junit.*;

public class SellQueryTests {
    int intOne = 1;

    SellQuery sellQueryZero = new SellQuery(1, 0, 10, 500, Query.BPLATE|Query.COVEL, 101);
    SellQuery sellQueryOne = new SellQuery(1, 0, 10, 500, Query.BPLATE|Query.COVEL, 102);
    SellQuery sellQueryTwo = new SellQuery(0, 0, 10, 500, Query.BPLATE|Query.COVEL, 103);
    SellQuery sellQueryThree = new SellQuery(1, -5, 10, 500, Query.BPLATE|Query.COVEL, 104);
    SellQuery sellQueryFour = new SellQuery(1, 0, 5, 500, Query.BPLATE|Query.COVEL, 105);
    SellQuery sellQueryFive = new SellQuery(1, 0, 10, 400, Query.BPLATE|Query.COVEL, 106);
    SellQuery sellQuerySix = new SellQuery(1, 0, 10, 500, Query.BPLATE, 107);
    SellQuery sellQuerySeven = new SellQuery(1, 0, 10, 500, Query.BPLATE|Query.COVEL|Query.DE_NEVE, 108);

    @Test
    public void equals_SameSellQuery () {
        //Tests if calling SellQuery.equals on another SellQuery object with the same values for all fields returns true
        Assert.assertTrue(sellQueryOne.equals(sellQueryZero));
    }

    @Test
    public void equals_DifferentObjects () {
        //Tests if calling SellQuery.equals on a non SellQuery object returns false
        Assert.assertFalse(sellQueryOne.equals(intOne));
    }

    @Test
    public void equals_DifferentUserId () {
        //Tests if calling SellQuery.equals on another SellQuery object with a different userId field returns false
        Assert.assertFalse(sellQueryOne.equals(sellQueryTwo));
    }

    @Test
    public void equals_DifferentTimeRangeStart () {
        //Tests if calling SellQuery.equals on another SellQuery object with a different timeRangeStart field returns false
        Assert.assertFalse(sellQueryOne.equals(sellQueryThree));
    }

    @Test
    public void equals_DifferentTimeRangeEnd () {
        //Tests if calling SellQuery.equals on another SellQuery object with a different timeRangeEnd field returns false
        Assert.assertFalse(sellQueryOne.equals(sellQueryFour));
    }

    @Test
    public void equals_DifferentPriceCents () {
        //Tests if calling SellQuery.equals on another SellQuery object with a different priceCents field returns false
        Assert.assertFalse(sellQueryOne.equals(sellQueryFive));
    }

    @Test
    public void equals_DifferentDiningHallBitfield () {
        //Tests if calling SellQuery.equals on another SellQuery object with a different diningHallBitfield field returns false
        Assert.assertFalse(sellQueryOne.equals(sellQuerySix));
        Assert.assertFalse(sellQueryOne.equals(sellQuerySeven));
    }
}
