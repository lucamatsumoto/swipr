package com.swipr.models;

import java.util.List;
import org.junit.*;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BuyerTests {
    private static Buyer alice, bob, eve;
    private static Seller eggert, kampe, campbell;

    @BeforeClass
    public static void setup() {
        alice = new Buyer("Alice", "A.","alice@cs.ucla.edu");
        bob = new Buyer("Bob", "B.", "bob@cs.ucla.edu");
        eve = new Buyer("Eve", "C.", "eve@cs.ucla.edu");
        eggert = new Seller("Paul", "Eggert", "eggert@cs.ucla.edu");
        kampe = new Seller("Mark", "Kampe", "kampe@cs.ucla.edu");
        campbell = new Seller("Michael", "Campbell", "campbell@cs.ucla.edu");

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
        List<Buyer> buyerList1 = eggert.getPotentialBuyers();
        Assert.assertTrue(buyerList1.contains(alice));
        Assert.assertTrue(buyerList1.contains(bob));
        Assert.assertTrue(buyerList1.contains(eve));

        List<Buyer> buyerList2 = kampe.getPotentialBuyers();
        Assert.assertTrue(buyerList2.contains(alice));
        Assert.assertTrue(buyerList2.contains(bob));
        Assert.assertTrue(buyerList2.contains(eve));

        List<Buyer> buyerList3 = campbell.getPotentialBuyers();
        Assert.assertTrue(buyerList3.contains(alice));
        Assert.assertTrue(buyerList3.contains(bob));
        Assert.assertTrue(buyerList3.contains(eve));
    }
}
