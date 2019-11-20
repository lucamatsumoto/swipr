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
    private static SellQuery sq1, sq2, sq3, sq4, sq5, sq6;

    @BeforeClass
    public static void setup() {
        asp = new AverageSwipePrice();
        sq1 = new SellQuery(1, 0, 10, 123, Query.BPLATE, 101);
        sq2 = new SellQuery(2, 11, 20, 456, Query.COVEL, 102);
        sq3 = new SellQuery(3, 21, 30, 789, Query.DE_NEVE, 103);
        sq4 = new SellQuery(4, 31, 40, 135, Query.DE_NEVE, 104);
        sq5 = new SellQuery(5, 41, 50, 246, Query.DE_NEVE, 105);
        sq6 = new SellQuery(6, 51, 60, 357, Query.DE_NEVE, 106);
    }

    @Test
    public void A_getCentsInitial() {
        Assert.assertEquals(0, asp.getCents());
    }

    @Test
    public void B_getPreviousInitial() {
        Assert.assertEquals(0, asp.getPrevious());
    }

    @Test
    public void C_getCentsAfterIncludingSQ1() {
        asp.includeSellQuery(sq1);
        Assert.assertEquals(123, asp.getCents());
    }

    @Test
    public void D_getCentsAfterIncludingSQ2() {
        asp.includeSellQuery(sq2);
        Assert.assertEquals(289, asp.getCents());
    }

    @Test
    public void E_getCentsAfterIncludingSQ3() {
        asp.includeSellQuery(sq3);
        Assert.assertEquals(456, asp.getCents());
    }

    @Test
    public void F_getCentsAfterFirstReset() {
        asp.reset();
        Assert.assertEquals(456, asp.getCents());
    }

    @Test
    public void H_getCentsAfterIncludingSQ456() {
        asp.includeSellQuery(sq4);
        Assert.assertEquals(135, asp.getCents());
        asp.includeSellQuery(sq5);
        Assert.assertEquals(190, asp.getCents());
        asp.includeSellQuery(sq6);
        Assert.assertEquals(246, asp.getCents());
    }

    @Test
    public void G_getPreviousAfterFirstReset() {
        Assert.assertEquals(456, asp.getPrevious());
    }

    @Test
    public void I_getCentsAfterSecondReset() {
        asp.reset();
        Assert.assertEquals(246, asp.getCents());
    }

    @Test
    public void J_getPreviousAfterSecondReset() {
        Assert.assertEquals(246, asp.getPrevious());
    }
}