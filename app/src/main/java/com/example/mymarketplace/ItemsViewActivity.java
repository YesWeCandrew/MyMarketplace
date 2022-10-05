package com.example.mymarketplace;

import android.content.res.AssetManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mymarketplace.Entities.CSVReader;
import com.example.mymarketplace.Entities.Items;

import java.io.IOException;
import java.io.InputStream;

public class ItemsViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_view);

        // Creating items.
        Items items = Items.getInstance();

        AssetManager am = this.getAssets();
        try {
            InputStream is = am.open("Items.csv");
            Items.itemsFromCSV(CSVReader.parseCsv(is));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(items);
    }
}