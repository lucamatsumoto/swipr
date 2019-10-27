package com.swipr.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

    public void addPotentialBuyer(Buyer buyer) {
        potentialBuyers.add(buyer);
    }

}