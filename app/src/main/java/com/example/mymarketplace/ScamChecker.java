package com.example.mymarketplace;


import com.example.mymarketplace.Entities.Items;
import com.example.mymarketplace.Entities.Sellers;

/**
 * This class evaluates an Items.Item object and returns a scam score or scam boolean. An item is
 * marked as a scam when it has a ScamScore >= SCAM_THRESHOLD, which is arbitrarily set.
 * @author Andrew Howes
 */
public class ScamChecker {

    // The SCAM_THRESHOLD. Items with getScamScore >= this number are marked as a scam.
    private static final int SCAM_THRESHOLD = 7;

    /**
     * Given the item and the SCAM_THRESHOLD, returns whether it is thought to be a scam, or not.
     * @param item the item to determine whether it is a scam
     * @return whether the item is thought to be a scam
     */
    public static boolean isScam(Items.Item item) {
        return getScamScore(item) >= SCAM_THRESHOLD;
    }

    /**
     * Given the item, return the evaluated ScamScore. This score is based on the maximum of:
     * - AverageReviews
     * - SellerName matching SellerID (to flag sellers impersonating another)
     * @param item the item to evaluate
     * @return the scam score from 0 (not scam) to 10 (scam)
     * @author Andrew Howes
     */
    private static int getScamScore(Items.Item item) {
        return Math.max(getScamScoreAverageReviews(item),getScamScoreSellerMatches(item));
    }

    /**
     * Determines a scam score (0-10) based only on average reviews. This identifies dodgy sellers.
     * No reviews = 10
     * 2.5/5 = 5
     * 5/5 = 0
     * @param item the item to evaluate
     * @return a scam score from 0 (not scam) to 10 (scam)
     * @author Andrew Howes
     */
    private static int getScamScoreAverageReviews(Items.Item item) {
        return (int) Math.round(10 - (item.averageRating * 2));
    }

    /**
     * Determines a scam score (0-10) based only on whether the sellers purported name matches
     * that of the seller's ID. This identifies fake sellers.
     * Name matches = 0
     * Name doesn't match = 10
     * @param item the item to evaluate
     * @return a scam score of 0 (not scam) or 10 (scam)
     * @author Andrew Howes
     */
    private static int getScamScoreSellerMatches(Items.Item item) {
        if (Sellers.getSellers().get(item.sellerID).name.equals(item.sellerName)) {
            return 0;
        }
        return 10;
    }

}
