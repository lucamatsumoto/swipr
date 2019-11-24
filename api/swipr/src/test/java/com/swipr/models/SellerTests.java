package com.swipr.models;

import java.util.HashSet;
import java.util.Set;
import org.junit.*;

public class SellerTests {
    private static Buyer alice, bob, eve;
    private static Seller eggert;

    @BeforeClass
    public static void setup() {
        alice = new Buyer(1, "Alice", "A.","alice@cs.ucla.edu");
        bob = new Buyer(2, "Bob", "B.", "bob@cs.ucla.edu");
        eve = new Buyer(3, "Eve", "C.", "eve@cs.ucla.edu");
        eggert = new Seller(4, "Paul", "Eggert", "eggert@cs.ucla.edu");

        eggert.addPotentialBuyer(alice);
        eggert.addPotentialBuyer(bob);
        eggert.addPotentialBuyer(eve);
    }

    @Test
    public void trivialAddPotentialBuyerTest() {
        Set<Buyer> buyerList = eggert.getPotentialBuyers();
        Assert.assertTrue(buyerList.contains(alice));
        Assert.assertTrue(buyerList.contains(bob));
        Assert.assertTrue(buyerList.contains(eve));
    }
}
