package com.swipr.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * JSON Object for buyer to send which includes preferred meetup time and preferred dining hall 
 * in case the offer contains more than one dining hall
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Information {

    @JsonProperty("meetTime")
    private long meetTime;

    @JsonProperty("preferredDiningHall")
    private long preferredDiningHall;

    @JsonProperty("buyer")
    private Buyer buyer;

    @JsonCreator
    public Information(long meetTime, long preferredDiningHall, Buyer buyer) {
        this.meetTime = meetTime;
        this.preferredDiningHall = preferredDiningHall;
        this.buyer = buyer;
    }

}