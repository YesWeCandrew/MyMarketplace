package com.example.mymarketplace.Search;

import com.example.mymarketplace.Entities.Items;

/**
 * This class creates a node of the AVL Tree
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

    public Node() {}

    public String getProductName(){return item.productName;}

    public Items.Item getItem(){return item;}

    @Override
    public String toString(){
        if(item == null){
            return "item null";
        }else {
            return item.toString();
        }
    }
}
