package com.swipr.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Seller class that represents the provided user information from Google, and their venmo account. 
 * This information is stored in our PostgreSQL database.
 * Sellers are able to post their offers and be matched to buyers
 */
@Data
@EqualsAndHashCode(callSuper=true)
@Entity
@NoArgsConstructor
public class Seller extends User {

    @Transient
    private Offer offer;
    @Transient
    private List<Buyer> potentialBuyers;

    public Seller(String firstName, String lastName, String email) {
        super(firstName, lastName, email);
        this.potentialBuyers = new ArrayList<>();
    }

    /**
     * Adds the potential matched buyer to this seller
     * @param buyer Matched buyer
     */
    public void addPotentialBuyer(Buyer buyer) {
        potentialBuyers.add(buyer);
    }

}