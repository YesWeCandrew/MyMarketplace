package Entities;

import java.util.LinkedList;

public class Items {

    private static Items instance;
    private LinkedList<Item> items;


    /**
     * Private constructor for use internally. Create an empty LinkedList of items.
     *
     * @author Andrew Howes
     */
    private Items() {
        items = new LinkedList<Item>();
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
     * @return the singleton instance's LinkedList of Item.
     *
     * @author Andrew Howes
     */
    public static LinkedList<Item> getItems() {
        return getInstance().items;
    }

    public boolean addItem(Item item) {
        return items.add(item);
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
        }
    }
}
