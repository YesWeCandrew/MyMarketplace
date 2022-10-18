package com.example.mymarketplace;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.example.mymarketplace.Entities.Database;
import com.example.mymarketplace.Entities.Users;
import com.example.mymarketplace.Helpers.Hasher;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class UserLoginTests {

    @BeforeClass
    public static void setUp() throws IOException {
        InputStream is = UserLoginTests.class.getClassLoader().getResourceAsStream("UsersTest.csv");
        Database.importData(is, Database.DataType.Users);
        is.close();
    }

    @Test
    public void SHA256HashingIsCorrect() {
        String string1 = "We're gonna smash this assignment";
        String string2 = "Please give us a good grade";
        String string3 = "Lets get this bread";

        String expected1 = "6a8123b45eb50388d0c73894aa9c5f11c727b9ea520844f4c1f5656679d0a359";
        String expected2 = "3c9898e212f88863ca00c7b833b2c568441fa648c440755b496745efd185f7c1";
        String expected3 = "f33b24ea7f0042bd6e894a163a8346c9ed6aabc2faf3c161531d5c1041a14424";

        assertEquals(expected1,Hasher.hash(string1));
        assertEquals(expected2,Hasher.hash(string2));
        assertEquals(expected3,Hasher.hash(string3));
    }

    @Test
    public void logInSuccessfully() {
        String username1 = "comp2100@anu.au";
        String password1 = "comp2100";

        String username2 = "comp6442@anu.au";
        String password2 = "comp6442";

        assertEquals(Users.getUsers().get(0),Users.userLoginValid(username1, Hasher.hash(password1)));
        assertEquals(Users.getUsers().get(1),Users.userLoginValid(username2, Hasher.hash(password2)));
    }

    @Test
    public void logInIncorrectDetails() {
        String username1 = "comp2100@anu.au";
        String password1 = "BAD PASSWORD";

        String username2 = "BAD USERNAME";
        String password2 = "comp6442";

        String username3 = "BAD USERNAME";
        String password3 = "BAD PASSWORD";

        assertNull(Users.userLoginValid(username1, Hasher.hash(password1)));
        assertNull(Users.userLoginValid(username2, Hasher.hash(password2)));
        assertNull(Users.userLoginValid(username3, Hasher.hash(password3)));
    }
}
