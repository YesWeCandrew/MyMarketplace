package com.example.mymarketplace.Search;

import android.util.Log;

import com.example.mymarketplace.Entities.Items;

/**
 * This class creates a AVL Tree based on the price of the item listed
 * Items with similar price will be stored in the same node
 * @author: Vincent Tanumihardja, Matthew Cawley
 * References:
 * - Code Structure: https://www.baeldung.com/java-avl-trees
 */
public class AVLTree {

    public Node root;

    public AVLTree(){
        root = new Node();
    }

    // updates the height of the tree
    void updateHeight(Node node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    // gets the height of the tree
    // height is -1 to distinguish root node and no node
    int height(Node node) {
        if (node == null) {
            return -1;
        }
        return node.height;
    }

    // get the balance factor of the tree
    int getBalanceFactor (Node node) {
        if (node == null) {
            return 0;
        }
        return height(node.right) - height(node.left);
    }

    // do a right rotate
    Node rotateRight(Node node) {
        Node l = node.left;
        Node lr = l.right;
        l.right = node;
        node.left = lr;
        updateHeight(node);
        updateHeight(l);
        return l;
    }

    // do a left rotate
    Node rotateLeft(Node node) {
        Node r = node.right;
        Node rl = r.left;
        r.left = node;
        node.right = rl;
        updateHeight(node);
        updateHeight(r);
        return r;
    }

    // adjust the nodes so that the tree is balance by rotating the tree
    Node balance(Node node) {
        updateHeight(node);
        int balanceFactor = getBalanceFactor(node);
        if (balanceFactor > 1) {
            if (height(node.right.right) > height(node.right.left)) {
                node = rotateLeft(node);
            }
            else {
                node.right = rotateRight(node.right);
                node = rotateLeft(node);
            }
        }
        else if (balanceFactor < -1) {
            if (height(node.left.left) > height(node.left.right)) {
                node= rotateLeft(node);
            }
            else {
                node.left = rotateRight(node.left);
                node = rotateRight(node);
            }
        }
        return node;
    }

    public void insert(Items.Item item){
        insert2(root, item);
    }

    // insert a new node if there is no matching price, insert on the left of the node if price is lower,
    // insert on right of the node if price is higher and only insert the attributes when price is similar
    // You are supposed to call this function with the root node originally, it will recurse down the tree
    Node insert2(Node node, Items.Item item) {
        if (node.item == null) {
            node = new Node(item);
        }else{
            if (node.getProductName().compareTo(item.productName) < 0) {
                node.left = insert2(node.left, item);
            } else if (node.getProductName().compareTo(item.productName) > 0) {
                node.right = insert2(node.right, item);
            }
        }
        return balance(node);
    }

    // return a list of items with the searched name, null if there is none
    public Node search(String name) {
        Log.i("h","h");
        Node current = root;
        Log.i("LOGPART2", current.toString());
        while (current != null) {
            if (current.item.productName.equals(name)) {
                break;
            }
            if (current.getProductName().compareTo(name) < 0){
                current = current.right;
            }
            else if (current.getProductName().compareTo(name) > 0){
                current = current.left;
            }
        }
        if (!current.item.productName.equals(name)) {
            return null;
        }
        return current;
    }
}
