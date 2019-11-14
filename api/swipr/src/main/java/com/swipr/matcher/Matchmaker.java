package com.swipr.matcher;

import java.util.ArrayList;

/** The Matchmaker class forms the core of our "business logic". It is
 *  a singleton mediator that
 *
 *  1. Keeps an in-memory database of all active requests to buy and
 *     sell swipes; these are represented with the immutable objects
 *     BuyQuery and SellQuery. Each user may have at most one active
 *     request (either buy or sell, but not both)
 *
 *  2. Finds matches between buy and sell requests, reporting them using
 *     the observer interface SellQueryListener (one SellQueryListener is
 *     associated with each BuyQuery registered with the matchmaker).
 */
public class Matchmaker {
    // Singleton Matchmaker.
    private static Matchmaker theInstance;

    // List of active seller queries (active = SellQueryListeners may
    // be informed of their existence).
    private final ArrayList<SellQuery> sellQueryList =
        new ArrayList<SellQuery>();

    // List of active buyer queries and their corresponding
    // SellQueryListeners. buyQueryList[i] corresponds to listenerList[i].
    private final ArrayList<BuyQuery> buyQueryList =
        new ArrayList<BuyQuery>();
    private final ArrayList<SellQueryListener> listenerList =
        new ArrayList<SellQueryListener>();

    /** Return the singleton instance of Matchmaker. */
    public static Matchmaker getInstance() {
        if (theInstance != null) return theInstance;
        return getInstanceSynchronized();
    }

    // Synchronized helper function for singleton.
    private static synchronized Matchmaker getInstanceSynchronized() {
        if (theInstance != null) return theInstance;
        return (theInstance = new Matchmaker());
    }

    /** Add (or replace) a BuyQuery to the database of active buy
     *  requests, and associate the given SellQueryListener with the
     *  new BuyQuery. The SellQueryListener will be notified of all
     *  SellQuery matches found for the given BuyQuery.
     *
     *  Replacement occurs iff the given BuyQuery has the same user id
     *  as some other active query stored in this Matchmaker. In this
     *  case, the old BuyQuery's associated SellQueryListener is
     *  deregistered, and updateBuyQuery returns true.
     */
    public synchronized boolean updateBuyQuery(
        BuyQuery newBuyQuery,
        SellQueryListener listener)
    {
        boolean didDelete = deleteByUserId(newBuyQuery.userId);

        // Recall 1-to-1 mapping of BuyQueries to SellQueryListener.
        assert(buyQueryList.size() == listenerList.size());

        // Add the new query and listener to the parallel lists.
        buyQueryList.add(newBuyQuery);
        listenerList.add(listener);

        // Notify the listener immediately of preexisting SellQueries
        // that match the Buy Query.
        int sz = sellQueryList.size();
        for (int i = 0; i < sz; ++i) {
            SellQuery thisSellQuery = sellQueryList.get(i);
            if (matches(thisSellQuery, newBuyQuery)) {
                listener.onMatchFound(thisSellQuery);
            }
        }

        return didDelete;
    }

    /** Add (or replace) a SellQuery to the database of active sell
     *  requests. This may cause some SellQueryListeners to be notified.
     *
     *  Replacement occurs iff this SellQuery has the same user id as
     *  some other active query stored in this Matchmaker --
     *  updateSellQuery returns true iff this replacement occurs.
     */
    public synchronized boolean updateSellQuery(SellQuery newSellQuery) {
        boolean didDelete = deleteByUserId(newSellQuery.userId);

        // Remember this new sell query.
        sellQueryList.add(newSellQuery);

        // Recall 1-to-1 mapping of BuyQueries to SellQueryListener.
        assert(buyQueryList.size() == listenerList.size());

        // Look for buy queries that match the newSellQuery, and notify
        // their corresponding listeners.
        int sz = buyQueryList.size();
        for (int i = 0; i < sz; ++i) {
            BuyQuery thisBuyQuery = buyQueryList.get(i);
            if (matches(newSellQuery, thisBuyQuery)) {
                SellQueryListener listener = listenerList.get(i);
                listener.onMatchFound(newSellQuery);
                // Optimization possibility: Instead of calling
                // listener.onMatchFound now (potentially expensive),
                // make a list of SellQueryListeners to notify and
                // call them outside of this synchronized function.
            }
        }

        return didDelete;
    }

    /** Deregisters any offers/bids with user id matching the given
     *  id.  This prevents further matches with those offers/bids from
     *  being made, informs listeners already matched of the
     *  cancellation, and deregisters the listener provided with the
     *  deleted offer/bid.
     *
     *  Returns true iff any such deletion occured.
     */
    public synchronized boolean deleteByUserId(long userId) {
        // Since there should only ever be 1 query (buy or sell) for a
        // given userId, we never expect do more than 1 deletion per
        // deleteByUserId call -- the assertions on !didDelete check for this.
        boolean didDelete = false;

        // Remove buy queries (and associated listeners) that match
        // the given user id. (Andrew: plural misleading?)
        for (int i = 0; i < buyQueryList.size(); ++i) {
            if (buyQueryList.get(i).userId == userId) {
                assert(!didDelete);
                didDelete = true;
                buyQueryList.remove(i);
                listenerList.remove(i);
            }
        }

        // Remove sell queries that match the given user id. When
        // we do this, we also have to notify listeners about the deletion.
        for (int i = 0; i < sellQueryList.size(); ++i) {
            SellQuery currentQuery = sellQueryList.get(i);
            if (currentQuery.userId == userId) {
                assert(!didDelete);
                didDelete = true;
                sellQueryList.remove(i);

                int sz = buyQueryList.size();
                for (int j = 0; j < sz; ++j) {
                    if (matches(currentQuery, buyQueryList.get(j))) {
                        listenerList.get(j).onMatchCancelled(currentQuery);
                    }
                }
            }
        }

        return didDelete;
    }

    // Returns true iff the buy query matches the sell query.
    private static boolean matches(SellQuery sellQuery, BuyQuery buyQuery) {
        // No overlapping dining halls?
        if ((sellQuery.diningHallBitfield & buyQuery.diningHallBitfield) == 0) {
            return false;
        }

        // Does the seller expect too high a price?
        if (sellQuery.priceCents > buyQuery.priceCents) {
            return false;
        }

        // https://stackoverflow.com/questions/3269434/whats-the-most-efficient-way-to-test-two-integer-ranges-for-overlap
        // All other requirements match; so match iff time ranges overlap.
        return sellQuery.timeRangeStart <= buyQuery.timeRangeEnd
            && buyQuery.timeRangeStart <= sellQuery.timeRangeEnd;
    }
}
