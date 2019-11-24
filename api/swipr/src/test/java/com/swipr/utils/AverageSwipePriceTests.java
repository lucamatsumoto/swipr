package com.swipr.utils;

import com.swipr.matcher.Query;
import com.swipr.matcher.SellQuery;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AverageSwipePriceTests {
    private static AverageSwipePrice asp;
    private static SellQuery sq1, sq2, sq3, sq4, sq5, sq6, sq7;

    @BeforeClass
    public static void setup() {
        asp = new AverageSwipePrice();
        sq1 = new SellQuery(1, 0, 10, 123, Query.BPLATE);
        sq2 = new SellQuery(2, 11, 20, 456, Query.COVEL);
        sq3 = new SellQuery(3, 21, 30, 789, Query.DE_NEVE);
        sq4 = new SellQuery(4, 22, 31, 420, Query.FEAST);
        sq5 = new SellQuery(5, 31, 40, 135, Query.FEAST);
        sq6 = new SellQuery(6, 41, 50, 246, Query.FEAST);
        sq7 = new SellQuery(7, 51, 60, 357, Query.FEAST);

        sq1.offerId = 1001;
        sq2.offerId = 1002;
        sq3.offerId = 1003;
        sq4.offerId = sq3.offerId;
        sq5.offerId = 1005;
        sq6.offerId = 1006;
        sq7.offerId = 1007;
    }

    @Test
    public void A_getCentsInitial() {
        //Checks initial value of the average swipe price
        Assert.assertEquals(0, asp.getCents());
    }

    @Test
    public void B_getPreviousInitial() {
        //Checks initial value of the "previous" average swipe price
        Assert.assertEquals(0, asp.getPrevious());
    }

    @Test
    public void C_getCentsAfterIncludingSQ1() {
        //Checks value of the average swipe price after adding sq1
        asp.includeSellQuery(sq1);
        Assert.assertEquals(123, asp.getCents());
    }

    @Test
    public void D_getCentsAfterIncludingSQ2() {
        //Checks value of the average swipe price after adding sq2
        asp.includeSellQuery(sq2);
        Assert.assertEquals(289, asp.getCents());
    }

    @Test
    public void E_getCentsAfterIncludingSQ3() {
        //Checks value of the average swipe price after adding sq3
        asp.includeSellQuery(sq3);
        Assert.assertEquals(456, asp.getCents());
    }

    @Test
    public void F_getCentsAfterIncludingSQ4() {
        //Checks value of the average swipe price after adding sq4
        //Because sq4 has the same offerId as sq3, this includeSellQuery operation should not affect the average swipe price
        asp.includeSellQuery(sq4);
        Assert.assertEquals(456, asp.getCents());
    }

    @Test
    public void G_getCentsAfterFirstReset() {
        //Checks if the average swipe price is equal to the "previous" average swipe price if there are currently no sell queries
        asp.reset();
        Assert.assertEquals(456, asp.getCents());
    }

    @Test
    public void H_getCentsAfterIncludingSQ4567() {
        //Checks value of the average swipe price after adding sq5, sq6, and sq7
        asp.includeSellQuery(sq4);
        Assert.assertEquals(420, asp.getCents());
        asp.includeSellQuery(sq5);
        Assert.assertEquals(277, asp.getCents());
        asp.includeSellQuery(sq6);
        Assert.assertEquals(267, asp.getCents());
        asp.includeSellQuery(sq7);
        Assert.assertEquals(289, asp.getCents());
    }

    @Test
    public void I_getPreviousAfterFirstReset() {
        //Checks if the "previous" average swipe price is equal to the average swipe price right before the first reset
        Assert.assertEquals(456, asp.getPrevious());
    }

    @Test
    public void J_getCentsAfterSecondReset() {
        //Checks if the average swipe price is equal to the "previous" average swipe price if there are currently no sell queries
        asp.reset();
        Assert.assertEquals(289, asp.getCents());
    }

    @Test
    public void K_getPreviousAfterSecondReset() {
        //Checks if the "previous" average swipe price is equal to the average swipe price right before the second reset
        asp.includeSellQuery(sq1);
        asp.includeSellQuery(sq3);
        asp.includeSellQuery(sq5);
        Assert.assertEquals(289, asp.getPrevious());
    }
}