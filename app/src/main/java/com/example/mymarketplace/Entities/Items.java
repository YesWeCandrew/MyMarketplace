package com.example.mymarketplace.Entities;

import java.util.ArrayList;
import java.util.List;

public class Items {

    private static Items instance;
    private ArrayList<Item> items;


    /**
     * Private constructor for use internally. Create an empty LinkedList of items.
     *
     * @author Andrew Howes
     */
    private Items() {
        items = new ArrayList<Item>();
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
                    row.get(8),
                    row.get(9)
            ));
        }
    }

    /**
     * @return the singleton instance's LinkedList of Item.
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
     */
    public static boolean addItem(Item item) {
        return getInstance().items.add(item);
    }

    public static class Item {
        public int itemID;
        public String productName;
        public String sellerName;
        public int sellerID;
        public String color;
        public double price;
        public String subcategory;
        public String category;
        public String description;
        public String image;
        public int quantity;

        public Item(int itemID, String productName, String sellerName, int sellerID, String color, double price, String subcategory, String category, String description, String image) {
            this.itemID = itemID;
            this.productName = productName;
            this.sellerName = sellerName;
            this.sellerID = sellerID;
            this.color = color;
            this.price = price;
            this.subcategory = subcategory;
            this.category = category;
            this.description = description;
            this.image = image;
            setQuantity();
        }

        /**
         * Will read the relevant entries in Stock and return the current quantity, but for now
         * Just returns 10
         *
         * @Author Andrew Howes
         */
        public void setQuantity() {
            this.quantity = 10;
        }
    }
}
