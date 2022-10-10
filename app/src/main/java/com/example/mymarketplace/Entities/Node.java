package com.example.mymarketplace.Entities;

import java.util.ArrayList;

/**
 * This class creates a node of an AVL Tree
 * Author: Vincent Tanumihardja
 */
public class Node {
    String productName;
    String sellerName;
    Category category;
    Subcategory subcategory;
    int price;
    int height;
    Node left;
    Node right;

    public Node(String pname, String sname, Category cat, Subcategory subcat, int price) {
        this.productName = pname;
        this.sellerName = sname;
        this.category = cat;
        this.subcategory = subcat;
        this.price = price;
    }

    public String getProductName(){return productName;}
}
