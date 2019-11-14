package com.swipr.matcher;

public class BuyQuery extends Query {
    /** This is the SellQueryListener that notified by the Matchmaker
     * of SellQuery matches on this BuyQuery. See Matchmaker class for
     * fuller explanation.
     */
    public final SellQueryListener listener;

    public BuyQuery(
        long userId,
        long timeRangeStart,
        long timeRangeEnd,
        long priceCents,
        long diningHallBitfield)
    {
        super(userId, timeRangeStart, timeRangeEnd, priceCents, diningHallBitfield);
        this.listener = null;
    }

    public BuyQuery(
        long userId,
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
