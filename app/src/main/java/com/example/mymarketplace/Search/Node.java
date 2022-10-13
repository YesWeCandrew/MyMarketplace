package com.example.mymarketplace.Search;

import com.example.mymarketplace.Entities.Items;

/**
 * This class creates a node of an AVL Tree
 * @author Vincent Tanumihardja, Matthew Cawley
 */
public class Node {
    Node left;
    Node right;
    int height;
    Items.Item item;

    public Node(Items.Item item) {
        this.item = item;
    }

    public String getProductName(){return item.productName;}

    public Items.Item getItem(){return item;}
}
