package com.swipr.matcher;

public class SellQuery extends Query {
    public SellQuery(
        int userId,
        long timeRangeStart,
        long timeRangeEnd,
        long priceCents,
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
