package com.swipr.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"potentialBuyers"})
public class Seller extends User {

    @JsonIgnore
    @Transient
    private Set<Information> potentialBuyers;

    @JsonIgnore
    @Transient
    private Map<Buyer, Information> buyerMap; 

    private Integer id;

    @JsonCreator
    public Seller(@JsonProperty("id") Integer id, @JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName, @JsonProperty("email") String email) {
        super(firstName, lastName, email);
        this.potentialBuyers = new HashSet<>();
        this.id = id;
        this.buyerMap = new HashMap<>();
    }

    /**
     * Retrieve information about a buyer's interest for an offer
     * @return a set of potential buyer's information regarding queries
     */
    public Set<Information> getPotentialBuyersInformation() {
        return potentialBuyers;
    }

    /**
     * Return a set of buyers that were matched to the sellers
     * @return set of buyers that were matched 
     */
    public Set<Buyer> getPotentialBuyers() {
        return buyerMap.keySet();
    }

    /**
     * Adds the potential matched buyer to this seller
     * @param buyer Matched buyer
     * @param meetTime the preferred meetup time of the buyer
     * @param preferredDiningHall the preferred dining hall of a particular buyer
     */
    public void addPotentialBuyer(Buyer buyer,long meetTime, long preferredDiningHall) {
        Information newInfo = new Information(meetTime, preferredDiningHall, buyer);
        buyerMap.put(buyer, newInfo);
        potentialBuyers.add(newInfo);
    }
    
    /**
     * Remove a potential buyer if they have cancelled interest
     * @param buyer the buyer to remove from the list of matched sellers
     */
    public void removePotentialBuyer(Buyer buyer) {
        Information infoToRemove = buyerMap.get(buyer);
        buyerMap.remove(buyer);
        potentialBuyers.remove(infoToRemove);
    }

    /**
     * Clear all of the potential buyers attached to this seller x
     */
    public void clearPotentialBuyers() {
        buyerMap.clear();
        potentialBuyers.clear();    
    }
}
