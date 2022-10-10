package com.example.mymarketplace;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mymarketplace.Entities.CSVReader;
import com.example.mymarketplace.Entities.Items;
import com.example.mymarketplace.Entities.Sellers;
import com.example.mymarketplace.Entities.Stocks;
import com.example.mymarketplace.Entities.Users;

import java.io.IOException;
import java.io.InputStream;

public class ItemsViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_view);

        int userID = getIntent().getIntExtra("userID",100);

        // Load all assets to the correct classes
        AssetManager am = this.getAssets();
        try {
            // Inputting items
            InputStream is = am.open("Items.csv");
            Items.itemsFromCSV(CSVReader.parseCsv(is));

            // Inputting sellers
            is = am.open("Sellers.csv");
            Sellers.sellersFromCSV(CSVReader.parseCsv(is));

            // Inputting stock
            is = am.open("Stock.csv");
            Stocks.stockFromCSV(CSVReader.parseCsv(is));

            // Getting the first batch of stock data and updating item stock
            Stocks.addBatch();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // JUST FOR EXAMPLES: REPLACE THIS WITH THE UI LONG
        Items.Item item0 = Items.getItems().get(0);

        Toast toast = Toast.makeText(getApplicationContext(), "Welcome, " + Users.getUsers().get(userID).givenName, Toast.LENGTH_LONG);
        toast.show();

        toast = Toast.makeText(
                getApplicationContext(),
                "First item:" + item0.productName + " has quantity " + item0.quantity,
                Toast.LENGTH_LONG);
        toast.show();

        // UPDATING QUANTITY WITH THE NEXT BATCH OF DATA. WE CAN CALL THIS WHEN SOMEONE HITS A REFRESH BUTTON
        Stocks.addBatch();

        toast = Toast.makeText(
                getApplicationContext(),
                "After update, first item has quantity " + item0.quantity,
                Toast.LENGTH_LONG);
        toast.show();

        
    }
}