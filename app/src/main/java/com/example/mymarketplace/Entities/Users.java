package com.example.mymarketplace.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class stores the user data type
 * @author: Andrew Howes
 */
public class Users {

    // Singleton instance of Users
    private static Users instance;

    private final ArrayList<User> users;


    /**
     * Private constructor for use internally. Create an empty LinkedList of users.
     * @author Andrew Howes
     */
    private Users() {
        users = new ArrayList<>();
    }

    /**
     * Returns the instance of user, ensuring it is always a singleton.
     * @return the singleton instance of the users
     * @author Andrew Howes
     */
    public static Users getInstance() {
        if (Users.instance == null) {
            Users.instance = new Users();
        }

        return Users.instance;
    }

    /**
     * Constructor that adds all of the users in the list of list from the csv
     * to the users instance.
     * @param csvAsListOfLists the output of CSVReader for the Users file.
     * @author Andrew Howes
     */
    static void usersFromCSV(List<List<String>> csvAsListOfLists) {
        for (List<String> row : csvAsListOfLists) {
            Users.addUser(new User(
                    Integer.parseInt(row.get(0)),
                    row.get(1),
                    row.get(2),
                    row.get(3),
                    row.get(4),
                    row.get(5),
                    Integer.parseInt(row.get(6)),
                    row.get(7),
                    row.get(8)
            ));
        }
    }

    /**
     * Get the list of users
     * @return the singleton instance's array list of User.
     * @author Andrew Howes
     */
    public static ArrayList<User> getUsers() {
        return getInstance().users;
    }

    /**
     * Adds an user to the singleton's user list
     * @param user the user to add
     * @author Andrew Howes
     */
     private static void addUser(User user) {
        getInstance().users.add(user);
    }

    // the user class that holds all user data
    public static class User implements Serializable {
        public int userID;
        public String gender;
        public String title;
        public String givenName;
        public String surname;
        public String state;
        public int zipCode;
        public String username;
        public String hashedPassword;
        public String photoDirectory;

        private User(int userID, String gender, String title, String givenName, String surname, String state, int zipCode, String username, String hashedPassword) {
            this.userID = userID;
            this.gender = gender;
            this.title = title;
            this.givenName = givenName;
            this.surname = surname;
            this.state = state;
            this.zipCode = zipCode;
            this.username = username;
            this.hashedPassword = hashedPassword;
            this.photoDirectory = "user" + userID;
        }
    }

    /**
     * When given a valid username and hashedPassword, this function returns the relevant user ID
     * If the password or username is incorrect, null is returned.
     * @param username the username to find
     * @param hashedPassword the SHA-256 hashed password of the user
     * @return the user ID if the username and hashed password is valid.
     * @author Andrew Howes
     */
    public static User userLoginValid(String username, String hashedPassword) {
        username = username.trim();
        hashedPassword = hashedPassword.trim();

        // Iterators over each user, and checks if there is a matching username.
        for (User user : Users.getUsers()) {
            if (user.username.equals(username)) {
                // If there is a matching username, check that the hashes match
                if (user.hashedPassword.equals(hashedPassword)) {
                    return user;
                } else {return null;}
            }
        }
        return null;
    }
}
