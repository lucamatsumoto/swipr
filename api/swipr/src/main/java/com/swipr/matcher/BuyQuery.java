package com.swipr.matcher;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuyQuery extends Query {
    /** This is the SellQueryListener that notified by the Matchmaker
     * of SellQuery matches on this BuyQuery. See Matchmaker class for
     * fuller explanation.
     */
    public final SellQueryListener listener;

    public BuyQuery(
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
        this.listener = null;
    }

    public BuyQuery(
        int userId,
        long timeRangeStart,
        long timeRangeEnd,
        long priceCents,
        long diningHallBitfield,
        SellQueryListener listener)
    {
        super(userId, timeRangeStart, timeRangeEnd, priceCents, diningHallBitfield);
        this.listener = listener;
    }

    @Override
    public boolean equals(Object otherObject) {
        BuyQuery other;
        try {
            other = (BuyQuery) otherObject;
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
