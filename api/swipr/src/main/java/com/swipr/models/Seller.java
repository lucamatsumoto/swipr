package com.swipr.models;

import java.util.Collection;
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

    //For unit tests, might be useful later on
    public Set<Information> getPotentialBuyersInformation() {
        return potentialBuyers;
    }

    public Set<Buyer> getPotentialBuyers() {
        return buyerMap.keySet();
    }

    /**
     * Adds the potential matched buyer to this seller
     * @param buyer Matched buyer
     */
    public void addPotentialBuyer(Buyer buyer,long meetTime, long preferredDiningHall) {
        Information newInfo = new Information(meetTime, preferredDiningHall, buyer);
        buyerMap.put(buyer, newInfo);
        potentialBuyers.add(newInfo);
    }
    
    public void removePotentialBuyer(Buyer buyer) {
        Information infoToRemove = buyerMap.get(buyer);
        buyerMap.remove(buyer);
        potentialBuyers.remove(infoToRemove);
    }

    public void clearPotentialBuyers() {
        buyerMap.clear();
        potentialBuyers.clear();    
    }
}
