package com.example.mymarketplace;

import static org.junit.Assert.assertEquals;
import static Entities.CSVReader.createItems;

import org.junit.Test;

import java.io.IOException;

import Entities.Items;

public class DataReaderTests {
        @Test
        public void CSVItems() {
            String string = "";
            try {
                createItems();
                string = Items.getItems().get(0).toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assertEquals("asdf",string);
        }
}

