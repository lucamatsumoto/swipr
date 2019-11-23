package com.swipr.models;

import java.util.List;
import org.junit.*;

public class SellerTests {
    private static Buyer alice, bob, eve;
    private static Seller eggert;

    @BeforeClass
    public static void setup() {
        alice = new Buyer("Alice", "A.","alice@cs.ucla.edu");
        bob = new Buyer("Bob", "B.", "bob@cs.ucla.edu");
        eve = new Buyer("Eve", "C.", "eve@cs.ucla.edu");
        eggert = new Seller("Paul", "Eggert", "eggert@cs.ucla.edu");

        eggert.addPotentialBuyer(alice);
        eggert.addPotentialBuyer(bob);
        eggert.addPotentialBuyer(eve);
    }

    @Test
    public void trivialAddPotentialBuyerTest() {
        List<Buyer> buyerList = eggert.getPotentialBuyers();
        Assert.assertTrue(buyerList.contains(alice));
        Assert.assertTrue(buyerList.contains(bob));
        Assert.assertTrue(buyerList.contains(eve));
    }
}
