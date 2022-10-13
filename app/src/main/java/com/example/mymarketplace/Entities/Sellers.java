package com.example.mymarketplace.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Sellers {

    // Singleton instance of Sellers
    private static Sellers instance;

    private final ArrayList<Seller> sellers;


    /**
     * Private constructor for use internally. Create an empty LinkedList of sellers.
     *
     * @author Andrew Howes
     */
    private Sellers() {
        sellers = new ArrayList<>();
    }

    /**
     * Returns the instance of seller, ensuring it is always a singleton.
     * @return the singleton instance of the sellers
     *
     * @author Andrew Howes
     */
    public static Sellers getInstance() {
        if (Sellers.instance == null) {
            Sellers.instance = new Sellers();
        }

        return Sellers.instance;
    }

    /**
     * Constructor that adds all of the sellers in the list of list from the csv
     * to the sellers instance.
     * @param csvAsListOfLists the output of CSVReader for the Sellers file.
     */
    public static void sellersFromCSV(List<List<String>> csvAsListOfLists) {
        for (List<String> row : csvAsListOfLists) {
            Sellers.addSeller(new Seller(
                    Integer.parseInt(row.get(0)),
                    row.get(1),
                    row.get(2),
                    row.get(3),
                    Integer.parseInt(row.get(4)),
                    row.get(5),
                    row.get(6),
                    Double.parseDouble(row.get(7)),
                    Double.parseDouble(row.get(8))
            ));
        }
    }

    /**
     * @return the singleton instance's array list of Seller.
     *
     * @author Andrew Howes
     */
    public static ArrayList<Seller> getSellers() {
        return getInstance().sellers;
    }

    /**
     * Adds an seller to the singleton's seller list
     * @param seller the seller to add
     * @return if the seller was successfully added
     * @author Andrew Howes
     */
    public static boolean addSeller(Seller seller) {
        return getInstance().sellers.add(seller);
    }

    /**
     * The seller class. Every seller is represented as an instance of this object.
     */
    public static class Seller implements Serializable {
        public int sellerID;
        public String name;
        public String address;
        public String suburb;
        public int postcode;
        public String state;
        public String country;
        public double latitude;
        public double longitude;
        public String addressAsText;

        /**
         * Internal constructor of a seller
         * @param sellerID the seller's ID
         * @param name the name of the seller
         * @param address the sellers street address
         * @param suburb the suburb of the seller
         * @param postcode the postcode of the seller
         * @param state the state of the seller
         * @param country the country of the seller
         * @param latitude the latitude of the address
         * @param longitude the longitude of the address
         * @author Andrew Howes
         */
        private Seller(int sellerID, String name, String address, String suburb, int postcode, String state, String country, double latitude, double longitude) {
            this.sellerID = sellerID;
            this.name = name;
            this.address = address;
            this.suburb = suburb;
            this.postcode = postcode;
            this.state = state;
            this.country = country;
            this.latitude = latitude;
            this.longitude = longitude;
            this.addressAsText = address + ", " + suburb + ", " + state;
        }
    }
}
