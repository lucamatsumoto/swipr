package com.swipr.models;

import com.swipr.matcher.Query;
import com.swipr.matcher.SellQuery;

import java.util.ArrayList;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BuyerTests {
    private static Buyer alice, bob, eve;
    private static Seller eggert, kampe, campbell;
    private static SellQuery sq1, sq2, sq3, sq4;

    @BeforeClass
    public static void setup() {
        alice = new Buyer(1, "Alice", "A.","alice@cs.ucla.edu");
        bob = new Buyer(2, "Bob", "B.", "bob@cs.ucla.edu");
        eve = new Buyer(3, "Eve", "C.", "eve@cs.ucla.edu");

        eggert = new Seller(4, "Paul", "Eggert", "eggert@cs.ucla.edu");
        kampe = new Seller(5, "Mark", "Kampe", "kampe@cs.ucla.edu");
        campbell = new Seller(6, "Michael", "Campbell", "campbell@cs.ucla.edu");

        sq1 = new SellQuery(4, 0, 10, 300, Query.COVEL);
        sq2 = new SellQuery(5, 5, 15, 400, Query.DE_NEVE|Query.FEAST);
        sq3 = new SellQuery(6, 10, 20, 500, Query.BPLATE|Query.COVEL|Query.DE_NEVE);
        sq4 = new SellQuery(4, 0, 10, 300, Query.COVEL);

        sq1.offerId = 1001;
        sq2.offerId = 1002;
        sq3.offerId = 1003;
        sq4.offerId = 1004;
    }

    @Test
    public void A_onMatchFound_Test() {
        //Checks if SellQuery objects gets added to a Buyer object's matchedSellQueries
        alice.onMatchFound(sq1);
        bob.onMatchFound(sq1);
        bob.onMatchFound(sq2);
        eve.onMatchFound(sq1);
        eve.onMatchFound(sq2);
        eve.onMatchFound(sq3);

        Assert.assertTrue(alice.getMatchedSellQueries().contains(sq1));
        Assert.assertFalse(alice.getMatchedSellQueries().contains(sq2));
        Assert.assertFalse(alice.getMatchedSellQueries().contains(sq3));
        Assert.assertTrue(bob.getMatchedSellQueries().contains(sq1));
        Assert.assertTrue(bob.getMatchedSellQueries().contains(sq2));
        Assert.assertFalse(bob.getMatchedSellQueries().contains(sq3));
        Assert.assertTrue(eve.getMatchedSellQueries().contains(sq1));
        Assert.assertTrue(eve.getMatchedSellQueries().contains(sq2));
        Assert.assertTrue(eve.getMatchedSellQueries().contains(sq3));
    }

    @Test
    public void B_onMatchCancelled_Test() {
        //Checks if SellQuery objects gets removed from a Buyer object's matchedSellQueries
        alice.onMatchCancelled(sq1);
        bob.onMatchCancelled(sq3);
        eve.onMatchCancelled(sq2);

        Assert.assertFalse(alice.getMatchedSellQueries().contains(sq1));
        Assert.assertFalse(alice.getMatchedSellQueries().contains(sq2));
        Assert.assertFalse(alice.getMatchedSellQueries().contains(sq3));
        Assert.assertTrue(bob.getMatchedSellQueries().contains(sq1));
        Assert.assertTrue(bob.getMatchedSellQueries().contains(sq2));
        Assert.assertFalse(bob.getMatchedSellQueries().contains(sq3));
        Assert.assertTrue(eve.getMatchedSellQueries().contains(sq1));
        Assert.assertFalse(eve.getMatchedSellQueries().contains(sq2));
        Assert.assertTrue(eve.getMatchedSellQueries().contains(sq3));
    }

    @Test
    public void C_clearMatchedSellQueries_Test() {
        //Checks if all SellQuery objects can removed from a Buyer object's matchedSellQueries simultaneously
        alice.clearMatchedSellQueries();
        bob.clearMatchedSellQueries();
        eve.clearMatchedSellQueries();

        Assert.assertTrue(alice.getMatchedSellQueries().isEmpty());
        Assert.assertTrue(bob.getMatchedSellQueries().isEmpty());
        Assert.assertTrue(eve.getMatchedSellQueries().isEmpty());
    }

    @Test
    public void D_indicateInterestInOffer_ExpiredSellQuery_Test() {
        //Checks if Buyer's cannot express interest in expired SellQuery objects (indicated by a change in offerId)
        Assert.assertFalse(eggert.getPotentialBuyers().contains(alice));
        alice.onMatchFound(sq1);
        alice.indicateInterestInOffer(sq4, eggert);
        Assert.assertFalse( eggert.getPotentialBuyers().contains(alice));
    }

    @Test
    public void E_indicateInterestInOffer_ValidSellQuery_Test() {
        //Checks if Buyer's can express interest in valid SellQuery objects (indicated by a change in offerId)
        Assert.assertTrue(eggert.getPotentialBuyers().isEmpty());
        Assert.assertTrue(kampe.getPotentialBuyers().isEmpty());
        Assert.assertTrue(campbell.getPotentialBuyers().isEmpty());

        alice.onMatchFound(sq1);
        bob.onMatchFound(sq1);
        bob.onMatchFound(sq2);
        eve.onMatchFound(sq1);
        eve.onMatchFound(sq2);
        eve.onMatchFound(sq3);

        alice.indicateInterestInOffer(sq1, eggert);
        bob.indicateInterestInOffer(sq1, eggert);
        bob.indicateInterestInOffer(sq2, kampe);
        eve.indicateInterestInOffer(sq1, eggert);
        eve.indicateInterestInOffer(sq2, kampe);
        eve.indicateInterestInOffer(sq3, campbell);

        Assert.assertTrue(eggert.getPotentialBuyers().contains(alice));
        Assert.assertTrue(eggert.getPotentialBuyers().contains(bob));
        Assert.assertTrue(eggert.getPotentialBuyers().contains(eve));
        Assert.assertFalse(kampe.getPotentialBuyers().contains(alice));
        Assert.assertTrue(kampe.getPotentialBuyers().contains(bob));
        Assert.assertTrue(kampe.getPotentialBuyers().contains(eve));
        Assert.assertFalse(campbell.getPotentialBuyers().contains(alice));
        Assert.assertFalse(campbell.getPotentialBuyers().contains(bob));
        Assert.assertTrue(campbell.getPotentialBuyers().contains(eve));
    }

    @Test
    public void F_cancelInterestInOffer_ExpiredSellQuery_Test() {
        //Checks if Buyer's cannot cancel interest in expired SellQuery objects (indicated by a change in offerId)
        alice.cancelInterestInOffer(sq4, eggert);
        Assert.assertTrue(eggert.getPotentialBuyers().contains(alice));
    }

    @Test
    public void G_cancelInterestInOffer_ValidSellQuery_Test() {
        //Checks if Buyer's can cancel interest in valid SellQuery objects (indicated by a change in offerId)
        alice.cancelInterestInOffer(sq1, eggert);
        bob.cancelInterestInOffer(sq2, kampe);
        eve.cancelInterestInOffer(sq3, campbell);

        Assert.assertFalse(eggert.getPotentialBuyers().contains(alice));
        Assert.assertTrue(eggert.getPotentialBuyers().contains(bob));
        Assert.assertTrue(eggert.getPotentialBuyers().contains(eve));
        Assert.assertFalse(kampe.getPotentialBuyers().contains(alice));
        Assert.assertFalse(kampe.getPotentialBuyers().contains(bob));
        Assert.assertTrue(kampe.getPotentialBuyers().contains(eve));
        Assert.assertFalse(campbell.getPotentialBuyers().contains(alice));
        Assert.assertFalse(campbell.getPotentialBuyers().contains(bob));
        Assert.assertFalse(campbell.getPotentialBuyers().contains(eve));
    }
}