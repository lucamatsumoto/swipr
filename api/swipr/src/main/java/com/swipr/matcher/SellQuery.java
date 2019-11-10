package com.swipr.matcher;

public class SellQuery extends Query {
    public SellQuery(
        long userId,
        long timeRangeStart,
        long timeRangeEnd,
        long priceCents,
        long diningHallBitfield)
    {
        super(userId, timeRangeStart, timeRangeEnd, priceCents, diningHallBitfield);
    }
}
