package com.example.mymarketplace;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mymarketplace.Entities.CSVReader;
import com.example.mymarketplace.Entities.Items;
import com.example.mymarketplace.Entities.Sellers;
import com.example.mymarketplace.Entities.Stocks;
import com.example.mymarketplace.Entities.Users;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class ItemsViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_view);

        Users.User user = getIntent().getSerializableExtra("user", Users.User.class);

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

        /**
         * Display User's name
         */

        ((TextView)findViewById(R.id.name)).setText(user.givenName);



        /**
         * ListView of all the product names
         */
        ListView itemListView = (ListView) findViewById(R.id.itemsListView);
        ArrayList<Items.Item> itemList = Items.getItems();
        ArrayList<String> productName = new ArrayList<>();

        for(int i=0; i<itemList.size(); i++) {
            Items.Item item = Items.getItems().get(i);
            String itemVal = item.productName;
            productName.add(itemVal);
        }

        ArrayAdapter myListAddapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, productName);

        itemListView.setAdapter(myListAddapter);
        itemListView.setDivider(null);
        itemListView.setClickable(true);
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ItemsViewActivity.this, ItemInfo.class);
                intent.putExtra("item",Items.getItems().get(position));
                startActivity(intent);
            }
        });
    }
}