package com.example.mymarketplace.Entities;

import java.util.ArrayList;

/**
 * This class creates a node of an AVL Tree
 * Author: Vincent Tanumihardja
 */
public class Node {
    int price;
    int height;
    ArrayList<ArrayList<String>> others;
    Node left;
    Node right;

    public Node(int price,ArrayList<String> others) {
        this.price = price;
        this.others.add(others);
    }
}
