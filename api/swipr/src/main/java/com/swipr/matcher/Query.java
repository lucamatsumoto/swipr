package com.swipr.matcher;

public class Query {
    // User ID of the user that created this query. NOT boxed because we
    // want to at least pretend we care about performance.
    public final long userId;

    // Start and end of the time range (in seconds since epoch) that
    // we are querying for.
    public final long timeRangeStart, timeRangeEnd;

    // Price in cents of the query.
    public final long priceCents;

    // Bitfield of dining halls selected for this query (bit on = selected.)
    public final long diningHallBitfield;

    public static final long BPLATE = 1, COVEL = 2, DE_NEVE = 4, FEAST = 8;

    public Query(
        long userId,
        long timeRangeStart,
        long timeRangeEnd,
        long priceCents,
        long diningHallBitfield)
    {
        this.userId = userId;
        this.timeRangeStart = timeRangeStart;
        this.timeRangeEnd = timeRangeEnd;
        this.priceCents = priceCents;
        this.diningHallBitfield = diningHallBitfield;
    }
}
