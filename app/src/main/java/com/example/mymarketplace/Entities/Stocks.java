package com.example.mymarketplace.Entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Stocks {

    // Singleton instance of Stock
    private static Stocks instance;

    // The instances that need access
    private ArrayList<Stock> stock;
    private ArrayList<Stock> stockStream;
    private HashMap<Integer, Integer> currentStock;
    private int batchNumber = 0;

    /**
     * Private constructor for use internally. Create an empty LinkedList of users.
     *
     * @author Andrew Howes
     */
    private Stocks() {
        stock = new ArrayList<>();
        stockStream = new ArrayList<>();
        currentStock = new HashMap<Integer, Integer>();
        // instantiates the hash map with 0 stock for every item from IDs 0 to 49)
        for (int i = 0; i < 50; i++) {
            currentStock.put(i,0);
        }
    }

    /**
     * Returns the instance of Stock, ensuring it is always a singleton.
     * @return the singleton instance of Stock
     *
     * @author Andrew Howes
     */
    public static Stocks getInstance() {
        if (Stocks.instance == null) {
            Stocks.instance = new Stocks();
        }

        return Stocks.instance;
    }

    /**
     * Constructor that adds all of the stock in the list of list from the csv
     * to the Stock parameter. Does not load any stock into currentStock.
     * @param csvAsListOfLists the output of CSVReader for the Stock file.
     */
    public static void stockFromCSV(List<List<String>> csvAsListOfLists) {
        for (List<String> row : csvAsListOfLists) {
            Stocks.getInstance().stock.add(new Stock(
                    Integer.parseInt(row.get(0)),
                    Integer.parseInt(row.get(1)),
                    row.get(2)
            ));
        }
    }

    /**
     * Pulls the next batch of data (50 rows) from the stock spreadsheet and loads into the
     * currently used data. Updates the currentStock HashMap with the latest total stock values.
     *
     * Needs to be called after the CSV is loaded in.
     * @author Andrew Howes
     */
    public static void addBatch() {
        int start = (getInstance().batchNumber * 50) % 3250;
        int end = ((getInstance().batchNumber + 1) * 50) % 3250;

        for (int i = start; i < end; i++) {
            Stock stock = getInstance().stock.get(i);
            getInstance().stockStream.add(stock);
            int prevStock = getInstance().currentStock.get(stock.itemID);
            getInstance().currentStock.put(stock.itemID,prevStock + stock.stockChange);
        }

        Items.updateQuantity();
    }

    /**
     * @return the current stock for the given ItemID
     *
     * @author Andrew Howes
     */
    public static int getCurrentStock(int ItemID) {
        return getInstance().currentStock.get(ItemID);
    }

    // the user class that holds all stock data
    public static class Stock {
        public int itemID;
        public int stockChange;
        public String timestamp;

        public Stock(int itemID, int stockChange, String timestamp) {
            this.itemID = itemID;
            this.stockChange = stockChange;
            this.timestamp = timestamp;
        }
    }
}
