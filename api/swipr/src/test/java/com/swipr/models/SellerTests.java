package com.swipr.models;

import java.util.Set;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SellerTests {
    private static Buyer alice, bob, eve;
    private static Seller eggert;

    @BeforeClass
    public static void setup() {
        alice = new Buyer(1, "Alice", "A.","alice@cs.ucla.edu");
        bob = new Buyer(2, "Bob", "B.", "bob@cs.ucla.edu");
        eve = new Buyer(3, "Eve", "C.", "eve@cs.ucla.edu");
        eggert = new Seller(4, "Paul", "Eggert", "eggert@cs.ucla.edu");
    }

    @Test
    public void A_addPotentialBuyer_Test() {
        eggert.addPotentialBuyer(alice, 0, 0);
        eggert.addPotentialBuyer(bob, 0, 0);
        eggert.addPotentialBuyer(eve, 0, 0);

        Assert.assertTrue(eggert.getPotentialBuyers().contains(alice));
        Assert.assertTrue(eggert.getPotentialBuyers().contains(bob));
        Assert.assertTrue(eggert.getPotentialBuyers().contains(eve));
    }

    @Test
    public void B_removePotentialBuyer_Test () {
        eggert.removePotentialBuyer(bob);

        Assert.assertTrue(eggert.getPotentialBuyers().contains(alice));
        Assert.assertFalse(eggert.getPotentialBuyers().contains(bob));
        Assert.assertTrue(eggert.getPotentialBuyers().contains(eve));
    }

    @Test
    public void C_clearPotentialBuyers_Test () {
        eggert.clearPotentialBuyers();

        Assert.assertFalse(eggert.getPotentialBuyers().contains(alice));
        Assert.assertFalse(eggert.getPotentialBuyers().contains(bob));
        Assert.assertFalse(eggert.getPotentialBuyers().contains(eve));
    }
}