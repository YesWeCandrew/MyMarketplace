package com.example.mymarketplace.Entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Reviews {

    // Singleton class
    private static Reviews instance;

    private final ArrayList<Reviews.Review> review; // Stores the list of all reviews, past and future
    private final HashMap<Integer, ReviewSummary> currentReviews; // Stores a summary of loaded reviews.
    public int batchNumber = 0;

    /**
     * Private constructor for use internally. Create an empty HashMap of items to average ratings.
     *
     * @author Andrew Howes
     */
    private Reviews() {
        review = new ArrayList<>();
        currentReviews = new HashMap<>();
        // instantiates the hash map with 0 stock for every item from IDs 0 to 49)
        for (int i = 0; i < 50; i++) {
            currentReviews.put(i,new ReviewSummary(0,0));
        }
    }

    /**
     * Returns the instance of Stock, ensuring it is always a singleton.
     * @return the singleton instance of Stock
     *
     * @author Andrew Howes
     */
    public static Reviews getInstance() {
        if (Reviews.instance == null) {
            Reviews.instance = new Reviews();
        }

        return Reviews.instance;
    }

    /**
     * Constructor that adds all of the review in the list of list from the csv
     * to the Review parameter. Does not load any stock into currentStock.
     * @param csvAsListOfLists the output of CSVReader for the Stock file.
     * @author Andrew Howes
     */
    static void reviewsFromCSV(List<List<String>> csvAsListOfLists) {
        for (List<String> row : csvAsListOfLists) {
            Reviews.getInstance().review.add(new Review(
                    Integer.parseInt(row.get(0)),
                    Integer.parseInt(row.get(2))
            ));
        }
    }

    /**
     * Pulls the next batch of data (50 rows) from the review attribute and loads into the
     * currently used data. Updates the currentStock HashMap with the latest total stock values.
     *
     * Needs to be called after the CSV is loaded in.
     * @author Andrew Howes
     */
    public static void addBatch() {
        int start = (getInstance().batchNumber * 50) % 700;
        int end = ((getInstance().batchNumber + 1) * 50) % 700;

        for (int i = start; i < end; i++) {
            Reviews.Review review = getInstance().review.get(i);
            ReviewSummary currentReview = getInstance().currentReviews.get(review.itemID);
            assert currentReview != null;
            int currentSum = currentReview.sumReviews;
            int currentCount = currentReview.countOfReviews;
            ReviewSummary updatedReviewSummary = new ReviewSummary(currentSum + review.review,currentCount+1);
            getInstance().currentReviews.put(review.itemID,updatedReviewSummary);
        }

        getInstance().batchNumber += 1;

        Items.updateReview();
    }

    /**
     * @return the current stock for the given ItemID
     *
     * @author Andrew Howes
     */
    public static double getCurrentReview(int ItemID) {
        ReviewSummary reviewSummary = getInstance().currentReviews.get(ItemID);
        assert reviewSummary != null;
        if (reviewSummary.countOfReviews <= 0) {
            return 0;
        } else return reviewSummary.sumReviews / (float) reviewSummary.countOfReviews;
    }

    /**
     * The class that holds individual review entities from the CSV file.
     */
    public static class Review {
        public int itemID;
        public int review;

        /**
         * The review constructor
         * @param itemID The item reviewed
         * @param review the review given (from 1 to 5)
         */
        public Review(int itemID, int review) {
            this.itemID = itemID;
            this.review = review;
        }
    }

    /**
     * This class holds a summary of the review information. This enables us to store less data in
     * the app, as all we need to know is the total sum of the reviews and the count of reviews so
     * that a mean can be derived
      */
    public static class ReviewSummary {
        public int sumReviews;
        public int countOfReviews;

        /**
         * A summary of all past reviews. Can be used to calculate average reviews to date.
         * @param sumReviews the sum of all reviews (from 1 to 5)
         * @param countOfReviews the number of reviews
         * @author Andrew Howes
         */
        public ReviewSummary(int sumReviews, int countOfReviews) {
            this.sumReviews = sumReviews;
            this.countOfReviews = countOfReviews;
        }
    }
}
