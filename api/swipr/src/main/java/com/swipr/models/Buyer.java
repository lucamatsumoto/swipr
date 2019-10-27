package com.swipr.models;

import javax.persistence.Entity;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

    public boolean searchOffer(int start, int end) {
        // implement some logic that interacts with a list of offers
        return true;
    }

}