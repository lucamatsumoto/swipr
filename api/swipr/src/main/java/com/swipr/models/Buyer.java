package com.swipr.models;

import javax.persistence.Entity;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.swipr.matcher.SellQueryListener;

import com.fasterxml.jackson.annotation.JsonCreator;
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
@JsonIgnoreProperties({"matchedSellQueries, matchedSellers"})
public class Buyer extends User implements SellQueryListener {

    @Transient
    private ArrayList<SellQuery> matchedSellQueries; // offer ids inside.

    private Integer id;

    @JsonCreator
    public Buyer(@JsonProperty("id") Integer id, @JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName, @JsonProperty("email") String email) {
        super(firstName, lastName, email);
        this.id = id;
        matchedSellQueries = new ArrayList<>();
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
     */
    @Override
    public void onMatchFound(SellQuery sellQuery) {
        matchedSellQueries.add(sellQuery);
    }

    /**
     *  When a SellQuery gets cancelled, Matchmaker calls this
     *  function to remove the SellQuery from the list.
     */
    @Override
    public void onMatchCancelled(SellQuery expiredSellQuery) {
        matchedSellQueries.remove(expiredSellQuery);
    }

    /**
     *  Clear the list of matched sell queries. Needed in case the
     *  user changes their buy query.
     */
    public void clearMatchedSellQueries() {
        matchedSellQueries.clear();
    }

    //For unit tests, might be useful later on
    public ArrayList<SellQuery> getMatchedSellQueries() {
        return matchedSellQueries;
    }

    /**
     *  Call this when the human buyer presses the "I'm interested"
     *  button for the given SellQuery. If the given SellQuery is
     *  still active and associated with a Seller, add this Buyer to
     *  the said Seller's list of potential buyers.
     */
    public void indicateInterestInOffer(SellQuery sellQuery, Seller seller) {
        long interestedOfferId = sellQuery.offerId;
        for (SellQuery sq : matchedSellQueries) {
            if (sq.offerId == interestedOfferId) {
                seller.addPotentialBuyer(this);
                return;
            }
        }
        // Throw exception maybe. We're not looking to do complex logic for this
        tellUserSorrySellQueryNotFound(sellQuery);
    }

    public void cancelInterestInOffer(SellQuery sellQuery, Seller seller) {
        long interestedOfferId = sellQuery.offerId;
        for (SellQuery sq : matchedSellQueries) {
            if (sq.offerId == interestedOfferId) {
                seller.removePotentialBuyer(this);
                return;
            }
        }
    }

    public void tellUserSorrySellQueryNotFound(SellQuery sellQuery) {
        // TODO: send notification to user that the SellQuery they were
        // interested in is no longer with us.
    }
}
