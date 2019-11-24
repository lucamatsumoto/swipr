package com.swipr.models;

import java.util.HashSet;
import java.util.Set;
import org.junit.*;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BuyerTests {
    private static Buyer alice, bob, eve;
    private static Seller eggert, kampe, campbell;

    @BeforeClass
    public static void setup() {
        alice = new Buyer(1, "Alice", "A.","alice@cs.ucla.edu");
        bob = new Buyer(2, "Bob", "B.", "bob@cs.ucla.edu");
        eve = new Buyer(3, "Eve", "C.", "eve@cs.ucla.edu");
        eggert = new Seller(4, "Paul", "Eggert", "eggert@cs.ucla.edu");
        kampe = new Seller(5, "Mark", "Kampe", "kampe@cs.ucla.edu");
        campbell = new Seller(6, "Michael", "Campbell", "campbell@cs.ucla.edu");

        eggert.addPotentialBuyer(alice);
        eggert.addPotentialBuyer(bob);
        eggert.addPotentialBuyer(eve);
        kampe.addPotentialBuyer(alice);
        kampe.addPotentialBuyer(bob);
        kampe.addPotentialBuyer(eve);
        campbell.addPotentialBuyer(alice);
        campbell.addPotentialBuyer(bob);
        campbell.addPotentialBuyer(eve);
    }

    @Test
    public void A_trivialAddPotentialBuyerTest() {
        Set<Buyer> buyerSet1 = eggert.getPotentialBuyers();
        Assert.assertTrue(buyerSet1.contains(alice));
        Assert.assertTrue(buyerSet1.contains(bob));
        Assert.assertTrue(buyerSet1.contains(eve));

        Set<Buyer> buyerSet2 = kampe.getPotentialBuyers();
        Assert.assertTrue(buyerSet2.contains(alice));
        Assert.assertTrue(buyerSet2.contains(bob));
        Assert.assertTrue(buyerSet2.contains(eve));

        Set<Buyer> buyerSet3 = campbell.getPotentialBuyers();
        Assert.assertTrue(buyerSet3.contains(alice));
        Assert.assertTrue(buyerSet3.contains(bob));
        Assert.assertTrue(buyerSet3.contains(eve));
    }
}
