package com.swipr.matcher;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class BuyQueryTests {
    int ONE = 1;
    private static BuyQuery bq0, bq1, bq2, bq3, bq4, bq5, bq6, bq7;

    @BeforeClass
    public static void setup() {
        bq0 = new BuyQuery(1, 0, 10, 500, Query.BPLATE | Query.COVEL);
        bq1 = new BuyQuery(1, 0, 10, 500, Query.BPLATE | Query.COVEL);
        bq2 = new BuyQuery(2, 0, 10, 500, Query.BPLATE | Query.COVEL);
        bq3 = new BuyQuery(1, 5, 10, 500, Query.BPLATE | Query.COVEL);
        bq4 = new BuyQuery(1, 0, 15, 500, Query.BPLATE | Query.COVEL);
        bq5 = new BuyQuery(1, 0, 10, 600, Query.BPLATE | Query.COVEL);
        bq6 = new BuyQuery(1, 0, 10, 500, Query.BPLATE);
        bq7 = new BuyQuery(1, 0, 10, 500, Query.BPLATE | Query.COVEL | Query.DE_NEVE);
    }

    @Test
    public void equals_SameBuyQuery_Test() {
        //Tests if calling BuyQuery.equals on another BuyQuery object with the same values for all fields returns true
        Assert.assertTrue(bq1.equals(bq0));
    }

    @Test
    public void equals_DifferentObjects_Test() {
        //Tests if calling BuyQuery.equals on a non BuyQuery object returns false
        Assert.assertFalse(bq1.equals(ONE));
    }

    @Test
    public void equals_DifferentUserId_Test() {
        //Tests if calling BuyQuery.equals on another BuyQuery object with a different userId field returns false
        Assert.assertFalse(bq1.equals(bq2));
    }

    @Test
    public void equals_DifferentTimeRangeStart_Test() {
        //Tests if calling BuyQuery.equals on another BuyQuery object with a different timeRangeStart field returns false
        Assert.assertFalse(bq1.equals(bq3));
    }

    @Test
    public void equals_DifferentTimeRangeEnd_Test() {
        //Tests if calling BuyQuery.equals on another BuyQuery object with a different timeRangeEnd field returns false
        Assert.assertFalse(bq1.equals(bq4));
    }

    @Test
    public void equals_DifferentPriceCents_Test() {
        //Tests if calling BuyQuery.equals on another BuyQuery object with a different priceCents field returns false
        Assert.assertFalse(bq1.equals(bq5));
    }

    @Test
    public void equals_DifferentDiningHallBitfield_Test() {
        //Tests if calling BuyQuery.equals on another BuyQuery object with a different diningHallBitfield field returns false
        Assert.assertFalse(bq1.equals(bq6));
        Assert.assertFalse(bq1.equals(bq7));
    }
}
