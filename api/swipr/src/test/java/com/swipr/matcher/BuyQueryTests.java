package com.swipr.matcher;

import org.junit.*;

public class BuyQueryTests {
    int intOne = 1;

    BuyQuery buyQueryZero = new BuyQuery(1, 0, 10, 500, Query.BPLATE|Query.COVEL);
    BuyQuery buyQueryOne = new BuyQuery(1, 0, 10, 500, Query.BPLATE|Query.COVEL);
    BuyQuery buyQueryTwo = new BuyQuery(2, 0, 10, 500, Query.BPLATE|Query.COVEL);
    BuyQuery buyQueryThree = new BuyQuery(1, 5, 10, 500, Query.BPLATE|Query.COVEL);
    BuyQuery buyQueryFour = new BuyQuery(1, 0, 15, 500, Query.BPLATE|Query.COVEL);
    BuyQuery buyQueryFive = new BuyQuery(1, 0, 10, 600, Query.BPLATE|Query.COVEL);
    BuyQuery buyQuerySix = new BuyQuery(1, 0, 10, 500, Query.BPLATE);
    BuyQuery buyQuerySeven = new BuyQuery(1, 0, 10, 500, Query.BPLATE|Query.COVEL|Query.DE_NEVE);


    @Test
    public void equals_SameBuyQuery () {
        //Tests if calling BuyQuery.equals on a BuyQuery object with the same values for all fields returns true
        Assert.assertTrue(buyQueryOne.equals(buyQueryZero));
    }

    @Test
    public void equals_DifferentObjects () {
        //Tests if calling BuyQuery.equals on another non BuyQuery object returns false
        Assert.assertFalse(buyQueryOne.equals(intOne));
    }

    @Test
    public void equals_DifferentUserId () {
        //Tests if calling BuyQuery.equals on another BuyQuery object with a different userId returns false
        Assert.assertFalse(buyQueryOne.equals(buyQueryTwo));
    }

    @Test
    public void equals_DifferentTimeRangeStart () {
        //Tests if calling BuyQuery.equals on another BuyQuery object with a different userId returns false
        Assert.assertFalse(buyQueryOne.equals(buyQueryThree));
    }

    @Test
    public void equals_DifferentTimeRangeEnd () {
        //Tests if calling BuyQuery.equals on another BuyQuery object with a different userId returns false
        Assert.assertFalse(buyQueryOne.equals(buyQueryFour));
    }

    @Test
    public void equals_DifferentPriceCents () {
        //Tests if calling BuyQuery.equals on another BuyQuery object with a different userId returns false
        Assert.assertFalse(buyQueryOne.equals(buyQueryFive));
    }

    @Test
    public void equals_DifferentDiningHallBitfield () {
        //Tests if calling BuyQuery.equals on another BuyQuery object with a different userId returns false
        Assert.assertFalse(buyQueryOne.equals(buyQuerySix));
        Assert.assertFalse(buyQueryOne.equals(buyQuerySeven));
    }
}
