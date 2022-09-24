# Initial code structure

This document describes the initial plan for our code structure.

## Data

Data will be stored as a CSV or JSON file. The following files will be created. The files will use primary and secondary keys to refer to other entities to enable lookups.

Users
- Username
- Name
- Password

Items
- Price
- Image
- Description
- Category
- Seller

Sellers
- Name
- Address

Reviews
- Timestamp
- Item
- Rating

Stock log
- Timestamp
- Item
- Change

## Java classes
Each of the items above will have their own class in Java. 

Once imported, these objects will be stored in a single class Marketplace. This is a use of the Facade design pattern.

Once imported, the objects will be stored as trees to enable fast look-up and finding.

## Java classes for views

We will create four Activities that will power the app. These are:

LoginActivity
- The first screen presented to the user
- Processes the username and password

ViewActivity
- Shows items
- Has a searchbar and filtering options to search with
- Default sorted by popularity

ItemActivity
- Shows information about an item including reviews, the seller, stock level

Seller Activity
- Information about the seller
- Shows the items that the seller sells.
