package com.swipr.utils;

import java.util.concurrent.atomic.AtomicLong;
import java.util.HashSet;

import com.swipr.matcher.SellQuery;

/** Functions for maintaining a global average swipe price. */
public class AverageSwipePrice {
    // Average (mean) equals total cents divided by sample count.
    private static AtomicLong totalCents = new AtomicLong(0);
    private static AtomicLong totalSamples = new AtomicLong(0);

    // Keep the previous calculated average around when we reset the
    // average.
    private static AtomicLong previousAverage = new AtomicLong(0);

    // Set of SellQuery offer IDs already included in the average --
    // used to avoid double counting.
    private static HashSet<Long> includedOfferIds = new HashSet<Long>();

    /** Return, in cents, the average price of a swipe.
     */
    public static long getCents() {
        // Conceptually, just divide total cents by total samples to
        // get average, unless we have 0 samples, in which case we
        // return the old average.  However, there's some rigamarole
        // about thread safety -- totalCents and totalSamples are
        // atomic but not atomic together, so if we notice a change we
        // have to retry the calculation.
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
        resetIncludedOfferIds();
    }

    private static synchronized void resetIncludedOfferIds() {
        includedOfferIds.clear();
    }

    /** Add a new price sample to the running average (using the price
     *  in the SellQuery). Avoids double counting using the set of
     *  seen offer id's.
     */
    public static void includeSellQuery(SellQuery sq) {
        if (includeOfferId(sq.offerId)) {
            totalCents.addAndGet(sq.priceCents);
            totalSamples.incrementAndGet();
        }
    }

    private static synchronized boolean includeOfferId(long offerId) {
        return includedOfferIds.add(offerId);
    }
}