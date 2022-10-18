package com.example.mymarketplace.Entities;

import com.example.mymarketplace.Helpers.CSVReader;

import java.io.InputStream;
import java.util.List;

/**
 * This class stores the enum data type used for the marketplace app
 * @author: Andrew Howes
 */
public class Database {

    public enum DataType {
        Items,
        Sellers,
        Users,
        Stock,
        Reviews
    }

    /**
     * Implements a facade for Items, Sellers, User, Stock and Reviews so there is a single
     * unified interface for loading.
     * @param is the input stream of the CSV to import
     * @param type the type of data to import
     * @author Andrew Howes
     */
    public static void importData(InputStream is, DataType type) {
        List<List<String>> csvAsListOfLists = CSVReader.parseCsv(is);

        if (type == DataType.Items) {
            Items.itemsFromCSV(csvAsListOfLists);
        } else if (type == DataType.Sellers) {
            Sellers.sellersFromCSV(csvAsListOfLists);
        } else if (type == DataType.Users) {
            Users.usersFromCSV(csvAsListOfLists);
        } else if (type == DataType.Stock) {
            Stocks.stockFromCSV(csvAsListOfLists);
        } else {
            Reviews.reviewsFromCSV(csvAsListOfLists);
        }
    }

    /**
     * Updates the data of stock levels and average reviews. Requires Stocks and Reviews to have
     * been loaded into the data already
     */
    public static void updateData() {
        Stocks.addBatch();
        Reviews.addBatch();
    }

}
