package com.swipr.models;

import javax.persistence.Entity;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.swipr.matcher.SellQueryListener;
import com.swipr.matcher.SellQuery;
import com.swipr.utils.AverageSwipePrice;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import com.swipr.repository.UserRepository;

/**
 * Buyer class that represents the provided user information from Google, and their venmo account.
 * This information is stored in our PostgreSQL database.
 * Buyers are able to search for existing offers.
 */
@Data
@EqualsAndHashCode(callSuper=true)
@Entity
@NoArgsConstructor
public class Buyer extends User implements SellQueryListener {
    @Autowired
    UserRepository userRepository;

    @Transient
    private ArrayList<SellQuery> matchedSellQueries; // offer ids inside.

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

    public ArrayList<SellQuery> getMatchedSellQueries() {
        return matchedSellQueries;
    }

    /**
     *  Call this when the human buyer presses the "I'm interested"
     *  button for the given SellQuery. If the given SellQuery is
     *  still active and associated with a Seller, add this Buyer to
     *  the said Seller's list of potential buyers.
     */
    public void indicateInterestInOffer(SellQuery sellQuery) {
        int userId = sellQuery.userId;
        long interestedOfferId = sellQuery.offerId;
        for (SellQuery sq : matchedSellQueries) {
            if (sq.offerId == interestedOfferId) {
                Seller seller = (Seller) userRepository.findById(userId);
                seller.addPotentialBuyer(this);
                AverageSwipePrice.includeSellQuery(sellQuery);
                return;
            }
        }
        tellUserSorrySellQueryNotFound(sellQuery);
    }

    public void tellUserSorrySellQueryNotFound(SellQuery sellQuery) {
        // TODO: send notification to user that the SellQuery they were
        // interested in is no longer with us.
    }
}
