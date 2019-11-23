package com.swipr.matcher;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

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

    /*
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
        long diningHallBitfield,
        // Check if this is optional
        @JsonProperty("offerId")
        long offerId)
    {
        super(userId, timeRangeStart, timeRangeEnd, priceCents, diningHallBitfield);
        this.offerId = offerId;
    } */ 

    // temp, for tests to compile.
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
            userId == other.userId &&
            timeRangeStart == other.timeRangeStart &&
            timeRangeEnd == other.timeRangeEnd &&
            priceCents == other.priceCents &&
            diningHallBitfield == other.diningHallBitfield;
    }
}
