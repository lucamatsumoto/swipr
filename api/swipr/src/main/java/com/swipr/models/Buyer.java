package com.swipr.models;

import javax.persistence.Entity;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.swipr.matcher.SellQueryListener;
import com.swipr.utils.UserSessionManager;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.swipr.matcher.SellQuery;
import java.util.ArrayList;

/**
 * Buyer class that represents the provided user information from Google, and their venmo account.
 * This information is stored in our PostgreSQL database.
 * Buyers are able to search for existing offers.
 */
@Data
@EqualsAndHashCode(callSuper=true)
@Entity
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"matchedSellQueries, userSessionManager"})
public class Buyer extends User implements SellQueryListener {

    @JsonIgnore
    @Transient
    private ArrayList<SellQuery> matchedSellQueries; // offer ids inside.

    // At this point the userSessionManager must already exist 
    @JsonIgnore
    @Transient
    private UserSessionManager userSessionManager = null;

    private Integer id;


    @JsonCreator
    public Buyer(@JsonProperty("id") Integer id, @JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName, @JsonProperty("email") String email) {
        super(firstName, lastName, email);
        this.id = id;
        matchedSellQueries = new ArrayList<>();
        userSessionManager = UserSessionManager.getInstance(null);
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

    /**
     *  When the matchmaker finds a matching SellQuery for this Buyer,
     *  add the SellQuery to the list of matched sell queries.
     * @param sellQuery the sellquery to add to the list of matched sellqueries for this buyer
     * @param update indicate whether or not a new offer was found for a particular buyer 
     */
    @Override
    public void onMatchFound(SellQuery sellQuery, boolean update) {
        try {
            // Check if the session manager has already been built. Otherwise we build it.
            if (update) {
                matchedSellQueries.add(sellQuery);
                userSessionManager.sendToUser(userSessionManager.getBuyerHeaders(this), "/queue/buyerFind", matchedSellQueries);
    
            } else {
                matchedSellQueries.add(sellQuery);
            }
        } catch (NullPointerException e) {
            // Should not happen but catch for now
            System.out.println(e.getLocalizedMessage());
        }

    }

    /**
     *  When a SellQuery gets cancelled, Matchmaker calls this
     *  function to remove the SellQuery from the list.
     * @param expiredSellQuery the sellQuery to remove from the list 
     */
    @Override
    public void onMatchCancelled(SellQuery expiredSellQuery) {
        try {
            // Let buyers know that the offer is not on the table anymore
            matchedSellQueries.remove(expiredSellQuery);
            userSessionManager.sendToUser(userSessionManager.getBuyerHeaders(this), "/queue/buyerFind", matchedSellQueries);
        } catch (NullPointerException e) {
            // Should not happen but catch for now
            System.out.println(e.getLocalizedMessage());
        }
    }

    /**
     *  Clear the list of matched sell queries. Needed in case the
     *  user changes their buy query.
     */
    public void clearMatchedSellQueries() {
        matchedSellQueries.clear();
    }

    /**
     * Get a list of matched offers 
     * @return a list of matched offers for a particular buyer
     */
    public ArrayList<SellQuery> getMatchedSellQueries() {
        return matchedSellQueries;
    }

    /**
     *  Call this when the human buyer presses the "I'm interested"
     *  button for the given SellQuery. If the given SellQuery is
     *  still active and associated with a Seller, add this Buyer to
     *  the said Seller's list of potential buyers.
     * @param sellQuery the sellquery to indicate interest in 
     * @param seller the seller that holds a particular sellquery
     * @param meetTime the preferred meet time of a particular buyer
     * @param preferredDiningHall the preferred dining hall of a particular buyer
     */
    public void indicateInterestInOffer(SellQuery sellQuery, Seller seller, long meetTime, long preferredDiningHall) {
        long interestedOfferId = sellQuery.offerId;
        for (SellQuery sq : matchedSellQueries) {
            if (sq.offerId == interestedOfferId) {
                seller.addPotentialBuyer(this, meetTime, preferredDiningHall);
                return;
            }
        }
    }

    /**
     * Called when a user wants to cancel their interest in a particular offer
     * @param sellQuery the offer that they would like to cancel interest in 
     * @param seller the seller that holds the particular offer
     */
    public void cancelInterestInOffer(SellQuery sellQuery, Seller seller) {
        long interestedOfferId = sellQuery.offerId;
        for (SellQuery sq : matchedSellQueries) {
            if (sq.offerId == interestedOfferId) {
                seller.removePotentialBuyer(this);
                return;
            }
        }
    }
}
