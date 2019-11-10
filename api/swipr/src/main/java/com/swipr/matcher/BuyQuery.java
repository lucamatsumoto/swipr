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
}
