package com.example.mymarketplace.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Items {

    // Singleton instance of Items
    private static Items instance;

    private final ArrayList<Item> items;


    /**
     * Private constructor for use internally. Create an empty LinkedList of items.
     *
     * @author Andrew Howes
     */
    private Items() {
        items = new ArrayList<>();
    }

    /**
     * Returns the instance of items, ensuring it is always a singleton.
     * @return the singleton instance of the items
     *
     * @author Andrew Howes
     */
    public static Items getInstance() {
        if (Items.instance == null) {
            Items.instance = new Items();
        }

        return Items.instance;
    }

    /**
     * Constructor that adds all of the items in the list of list from the csv
     * to the items instance.
     * @param csvAsListOfLists the output of CSVReader for the Items file.
     */
    public static void itemsFromCSV(List<List<String>> csvAsListOfLists) {
        for (List<String> row : csvAsListOfLists) {
            addItem(new Item(
                    Integer.parseInt(row.get(0)),
                    row.get(1),
                    row.get(2),
                    Integer.parseInt(row.get(3)),
                    row.get(4),
                    Integer.parseInt(row.get(5)),
                    row.get(6),
                    row.get(7),
                    row.get(8)
            ));
        }
    }

    /**
     * @return the singleton instance's array list of Item.
     *
     * @author Andrew Howes
     */
    public static ArrayList<Item> getItems() {
        return getInstance().items;
    }

    /**
     * Adds an item to the singleton's item list
     * @param item the item to add
     * @return if the item was successfully added
     * @author Andrew Howes
     */
    public static boolean addItem(Item item) {return getInstance().items.add(item);}

    /**
     * Reads the currentStock for the item from the Stock singleton and stores it within the class
     *
     * @author Andrew Howes
     */
    public static void updateQuantity() {
        for (Item item : getItems()) {
            item.quantity = Stocks.getCurrentStock(item.itemID);
        }
    }

    /**
     * The item class. Every item is represented as an instance of this object.
     */
    public static class Item implements Serializable {
        public int itemID;
        public String productName;
        public String sellerName;
        public int sellerID;
        public String color;
        public int price;
        public String subcategory;
        public String category;
        public String description;
        public int quantity;
        public double averageRating;
        public String photoDirectory;

        /**
         * The internal constructor for an item
         * @param itemID the itemsID
         * @param productName the name of the product
         * @param sellerName the name of the seller
         * @param sellerID the sellers ID
         * @param color the colour of the product
         * @param price the price of the product
         * @param subcategory the subcategory of the product
         * @param category the category of the product
         * @param description the description of the product
         * @author Andrew Howes
         */
        private Item(int itemID, String productName, String sellerName, int sellerID, String color, int price, String subcategory, String category, String description) {
            this.itemID = itemID;
            this.productName = productName;
            this.sellerName = sellerName;
            this.sellerID = sellerID;
            this.color = color;
            this.price = price;
            this.subcategory = subcategory;
            this.category = category;
            this.description = description;
            this.quantity = 0; // Quantity equal to zero at initialisation, updated by updateQuantity().
            this.averageRating = 0; // Average rating set to zero, will be updated by updateRating()
            this.photoDirectory = "item" + itemID;
        }
    }
}
