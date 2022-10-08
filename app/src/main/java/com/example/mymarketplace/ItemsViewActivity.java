package com.example.mymarketplace;

import android.content.res.AssetManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mymarketplace.Entities.CSVReader;
import com.example.mymarketplace.Entities.Items;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ItemsViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_view);

        AssetManager am = this.getAssets();
        try {
            InputStream is = am.open("Items.csv");
            Items.itemsFromCSV(CSVReader.parseCsv(is));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // The array list to display
        ArrayList<Items.Item> items = Items.getItems();

        // demo code to get the productName of the first item.
        // items.get(0).productName
    }
}