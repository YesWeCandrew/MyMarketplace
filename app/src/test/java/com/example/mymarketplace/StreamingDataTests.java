package com.example.mymarketplace;

import static org.junit.Assert.assertTrue;

import com.example.mymarketplace.Entities.Database;
import com.example.mymarketplace.Entities.Reviews;
import com.example.mymarketplace.Entities.Stocks;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;

public class StreamingDataTests {

    @BeforeClass
    public static void setUp() throws IOException {
        InputStream is = StreamingDataTests.class.getClassLoader().getResourceAsStream("ReviewsTest.csv");
        Database.importData(is, Database.DataType.Reviews);
        is.close();

        is = StreamingDataTests.class.getClassLoader().getResourceAsStream("StockTest.csv");
        Database.importData(is, Database.DataType.Stock);
        is.close();
    }

    @Test
    public void cyclesBackToStartWhenAllStockItemsProcessed() {
        IntStream.range(0, 100).forEach(i -> Stocks.addBatch());
        for (int i = 0; i < 50; i++) {
            assertTrue("Current stock should always be >= 0, but for Item " + i + "it is not",
                    Stocks.getCurrentStock(i) >= 0);
        }
    }

    @Test
    public void cyclesAndRemainsGTE1andLTE5() {
        IntStream.range(0, 100).forEach(i -> Reviews.addBatch());
        for (int i = 0; i < 50; i++) {
            assertTrue("Current reviews should always be >= 1 and <= 5, but for Item " + i + "it is not",
                    Reviews.getCurrentReview(i) >= 1 && Reviews.getCurrentReview(i) <= 5);
        }
    }
}
