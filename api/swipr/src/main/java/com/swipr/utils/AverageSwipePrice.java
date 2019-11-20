package com.swipr.utils;

import java.util.concurrent.atomic.AtomicLong;

import com.swipr.matcher.SellQuery;

/** Functions for maintaining a global average swipe price. */
public class AverageSwipePrice {
    // Average (mean) equals total cents divided by sample count.
    private static AtomicLong totalCents = new AtomicLong(0);
    private static AtomicLong totalSamples = new AtomicLong(0);

    // Keep the previous calculated average around when we reset the
    // average.
    private static AtomicLong previousAverage = new AtomicLong(0);

    // This is incremented each time the average is reset. This is
    // used to avoid double-counting a SellQuery in the running
    // average, but DO count a SellQuery again after a reset.
    private static AtomicLong averageUniqueId = new AtomicLong(0);

    /** Return, in cents, the average price of a swipe.
     */
    public static long getCents() {
        // Conceptually, just divide total cents by total samples to
        // get average, unless we have 0 samples, in which case we
        // return the old average.  However, there's some rigamarole
        // about thread safety -- totalCents and totalSamples
        int maxTries = 40;
        for (int i = 0; ; ++i) {
            long cents = totalCents.get();
            long samples = totalSamples.get();
            if (totalCents.get() == cents || i >= maxTries) {
                return samples == 0 ? previousAverage.get() : cents / samples;
            }
        }
    }
    
    /** Return the previous average (before reset() called).
     */
    public static long getPrevious() {
        return previousAverage.get();
    }

    /** Clear out all the samples used to calculate the average
     *  (i.e. reset the running counts of total cents paid and total
     *  sample count).
     */
    public static void reset() {
        previousAverage.set(getCents());
        totalCents.set(0);
        totalSamples.set(0);
        averageUniqueId.incrementAndGet();
    }

    /** Add a new price sample to the running average (using the price
     *  in the SellQuery). Avoids double counting using the magic
     *  SellQuery.averageUniqueId field.
     */
    public static void includeSellQuery(SellQuery sq) {
        if (sq.averageUniqueId == averageUniqueId.get()) return;
        sq.averageUniqueId = averageUniqueId.get();

        totalCents.addAndGet(sq.priceCents);
        totalSamples.incrementAndGet();
    }

    public static void excludeSellQuery(SellQuery sq) {
        if (sq.averageUniqueId == averageUniqueId.get()) return;
        sq.averageUniqueId = averageUniqueId.get();

        totalCents.addAndGet(-1 * sq.priceCents);
        totalSamples.decrementAndGet();
    }

}
