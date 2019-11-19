package com.swipr.matcher;

public class SellQuery extends Query {
    final public long offerId;

    // Needed for calculating average price of sell offers that
    // someone indicated interest in. Think twice before piggybacking
    // on this for other purposes.
    public long averageUniqueId = -1;

    public SellQuery(
        int userId,
        long timeRangeStart,
        long timeRangeEnd,
        long priceCents,
        long diningHallBitfield,
        long offerId)
    {
        super(userId, timeRangeStart, timeRangeEnd, priceCents, diningHallBitfield);
        this.offerId = offerId;
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
