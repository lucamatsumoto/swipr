package com.swipr.matcher;

public class BuyQuery extends Query {
    public BuyQuery(
        long userId,
        long timeRangeStart,
        long timeRangeEnd,
        long priceCents,
        long diningHallBitfield)
    {
        super(userId, timeRangeStart, timeRangeEnd, priceCents, diningHallBitfield);
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
