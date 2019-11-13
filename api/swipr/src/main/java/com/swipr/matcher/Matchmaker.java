package com.swipr.matcher;

import java.util.ArrayList;

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

    // Return the singleton instance of Matchmaker.
    public static Matchmaker getInstance() {
        if (theInstance != null) return theInstance;
        return getInstanceSynchronized();
    }

    // Synchronized helper function for singleton.
    private static synchronized Matchmaker getInstanceSynchronized() {
        if (theInstance != null) return theInstance;
        return (theInstance = new Matchmaker());
    }

    // Register the SellQueryListener with the Matchmaker, using query
    // info from the given BuyQuery. Deregisters any queries with the
    // same user id as the given BuyQuery.  Returns true iff any such
    // queries were deleted.
    public synchronized boolean updateBuyQuery(
        BuyQuery newBuyQuery,
        SellQueryListener listener)
    {
        boolean didDelete = deleteByUserId(newBuyQuery.userId);

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

    // Add the given sell query to the list of candidate sell queries
    // for buy/sell matches.
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
            }
        }

        return didDelete;
    }

    // Deregisters any offers/bids with user id matching the given id.
    // This prevents further matches with those offers/bids from being made,
    // informs listeners already matched of the cancellation, and deregisters
    // the listener provided with the deleted offer/bid.
    //
    // Returns true iff any such deletion occured. Since there should only
    // ever be 1 query (buy or sell) for a given userId, we never expect
    // do more than 1 deletion per deleteByUserId call -- the assertions
    // below check for this.
    public synchronized boolean deleteByUserId(long userId) {
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
