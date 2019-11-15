package com.swipr.models;

import javax.persistence.Entity;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Buyer class that represents the provided user information from Google, and their venmo account. 
 * This information is stored in our PostgreSQL database.
 * Buyers are able to search for existing offers.
 */
@Data
@EqualsAndHashCode(callSuper=true)
@Entity
@NoArgsConstructor
public class Buyer extends User {

    @Transient
    private Seller matchedSeller;

    public Buyer(String firstName, String lastName, String email) {
        super(firstName, lastName, email);
    }

    /**
     * Searches for an offer that matches the user's specifications
     * @param start the start time of the request
     * @param end the end time of the request
     * @return whether an offer was successfully found
     */
    public boolean searchOffer(long start, long end) {
        // implement some logic that interacts with a list of offers
        return true;
    }

}