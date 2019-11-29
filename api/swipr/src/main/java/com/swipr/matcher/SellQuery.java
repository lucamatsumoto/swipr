package com.swipr.matcher;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Represents an Offer that was posted by a particular seller.
 * Buyers will be matched to a user with a particular sell query if there parameters match. 
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"averageUniqueId"})
public class SellQuery extends Query {

    @JsonProperty
    public long offerId;
    // Needed for calculating average price of sell offers that
    // someone indicated interest in. Think twice before piggybacking
    // on this for other purposes.
    public long averageUniqueId = -1;

    @JsonCreator
    public SellQuery(
        @JsonProperty("userId")
        int userId,
        @JsonProperty("timeRangeStart")
        long timeRangeStart,
        @JsonProperty("timeRangeEnd")
        long timeRangeEnd,
        @JsonProperty("priceCents")
        long priceCents,
        @JsonProperty("diningHallBitfield")
        long diningHallBitfield)
    {
        super(userId, timeRangeStart, timeRangeEnd, priceCents, diningHallBitfield);
    }

    @Override
    public boolean equals(Object otherObject) {
        SellQuery other;
        try {
            other = (SellQuery) otherObject;
        } catch (ClassCastException ignored) {
            return false;
        }

        return
            offerId == other.offerId &&
            userId == other.userId &&
            timeRangeStart == other.timeRangeStart &&
            timeRangeEnd == other.timeRangeEnd &&
            priceCents == other.priceCents &&
            diningHallBitfield == other.diningHallBitfield;
    }
}
