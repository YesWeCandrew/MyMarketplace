package com.example.mymarketplace;

import static org.junit.Assert.assertEquals;

import com.example.mymarketplace.Entities.Database;
import com.example.mymarketplace.Entities.Items;
import com.example.mymarketplace.Entities.Reviews;
import com.example.mymarketplace.Entities.Sellers;
import com.example.mymarketplace.Entities.Stocks;
import com.example.mymarketplace.Entities.Users;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ImportingFromCSVTests {

    @Test
    public void importsUsers() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("UsersTest.csv");
        Database.importData(is, Database.DataType.Users);
        is.close();

        assertEquals("comp2100@anu.au",Users.getUsers().get(0).username);
        assertEquals("comp6442@anu.au",Users.getUsers().get(1).username);
        assertEquals(50,Users.getUsers().size());
    }

    @Test
    public void importsSellers() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("SellersTest.csv");
        Database.importData(is, Database.DataType.Sellers);
        is.close();

        assertEquals("Reverto",Sellers.getSellers().get(0).name);
        assertEquals("Smithington Cress",Sellers.getSellers().get(1).name);
        assertEquals(6, Sellers.getSellers().size());
    }

    @Test
    public void importsItems() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("ItemsTest.csv");
        Database.importData(is, Database.DataType.Items);
        is.close();

        assertEquals("MP3 Player Silver",Items.getItems().get(0).productName);
        assertEquals("Pulse Smart Pen White",Items.getItems().get(4).productName);
        assertEquals(50, Items.getItems().size());
    }

    @Test
    public void stockLevelsUpdate() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("StockTest.csv");
        Database.importData(is, Database.DataType.Stock);
        is.close();

        assertEquals(0,Stocks.getCurrentStock(0));
        assertEquals(0,Stocks.getCurrentStock(1));
        assertEquals(0,Stocks.getCurrentStock(2));

        Stocks.addBatch();

        assertEquals(30,Stocks.getCurrentStock(0));
        assertEquals(16,Stocks.getCurrentStock(1));
        assertEquals(6,Stocks.getCurrentStock(2));

        Stocks.addBatch();

        assertEquals(30,Stocks.getCurrentStock(0));
        assertEquals(25,Stocks.getCurrentStock(1));
        assertEquals(10,Stocks.getCurrentStock(2));
    }

    @Test
    public void reviewsAndStockUpdate() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("ReviewsTest.csv");
        Database.importData(is, Database.DataType.Reviews);
        is.close();

        is = this.getClass().getClassLoader().getResourceAsStream("StockTest.csv");
        Database.importData(is, Database.DataType.Stock);
        is.close();

        assertEquals(0,Reviews.getCurrentReview(0),0.001); // 0, 0, 3.0
        assertEquals(0,Reviews.getCurrentReview(1),0.001); // 0, 1.0, 1.333
        assertEquals(0,Reviews.getCurrentReview(2),0.001); // 0, 3.5, 3.5
        assertEquals(0,Reviews.getCurrentReview(0),0.001); // 0, 0, 3.0
        assertEquals(0,Reviews.getCurrentReview(1),0.001); // 0, 1.0, 1.333
        assertEquals(0,Reviews.getCurrentReview(2),0.001); // 0, 3.5, 3.5

        Database.updateData();

        assertEquals(0,Reviews.getCurrentReview(0),0.001); // 0, 0, 2.0
        assertEquals(1.0,Reviews.getCurrentReview(1),0.001); // 0, 1.0, 1.333
        assertEquals(3.5,Reviews.getCurrentReview(2),0.001); // 0, 3.5, 3.5
        assertEquals(0,Reviews.getCurrentReview(0),0.001); // 0, 0, 2.0
        assertEquals(1.0,Reviews.getCurrentReview(1),0.001); // 0, 1.0, 1.333
        assertEquals(3.5,Reviews.getCurrentReview(2),0.001); // 0, 3.5, 3.5

        Database.updateData();

        assertEquals(2.0,Reviews.getCurrentReview(0),0.001); // 0, 0, 3.0
        assertEquals(1.333,Reviews.getCurrentReview(1),0.001); // 0, 1.0, 1.333
        assertEquals(3.5,Reviews.getCurrentReview(2),0.001); // 0, 3.5, 3.5
        assertEquals(2.0,Reviews.getCurrentReview(0),0.001); // 0, 0, 3.0
        assertEquals(1.333,Reviews.getCurrentReview(1),0.001); // 0, 1.0, 1.333
        assertEquals(3.5,Reviews.getCurrentReview(2),0.001); // 0, 3.5, 3.5
    }
}