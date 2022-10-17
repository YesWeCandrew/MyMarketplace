package com.example.mymarketplace;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mymarketplace.Entities.Database;
import com.example.mymarketplace.Entities.Reviews;
import com.example.mymarketplace.Helpers.CSVReader;
import com.example.mymarketplace.Entities.Items;
import com.example.mymarketplace.Entities.Sellers;
import com.example.mymarketplace.Entities.Stocks;
import com.example.mymarketplace.Entities.Users;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ItemsViewActivity extends AppCompatActivity {

    private SwipeRefreshLayout swiperefresh;
    private ArrayAdapter myListAdapter;

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
            Database.importData(is, Database.DataType.Items);

            // Inputting sellers
            is = am.open("Sellers.csv");
            Database.importData(is, Database.DataType.Sellers);

            // Inputting stock
            is = am.open("Stock.csv");
            Database.importData(is, Database.DataType.Stock);

            // Inputting
            is = am.open("Reviews.csv");
            Database.importData(is, Database.DataType.Reviews);

            // Getting the first batch of stock data and updating item stock
            Database.updateData();

            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * Display User's name
         */
        ((TextView)findViewById(R.id.name)).setText(user.givenName);

        // Getting ImageView for the user profile picture
        ImageView sellerImageView = findViewById(R.id.userImageView);

        // Getting photo directory of the user
        int userPhotoDir = getResources().getIdentifier(user.photoDirectory,"drawable", getPackageName());

        // Set the user image
        sellerImageView.setImageResource(userPhotoDir);


        /**
         * ListView of all the product names
         */
        ListView itemListView = (ListView) findViewById(R.id.itemsListView);
        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        ArrayList<Items.Item> itemList = Items.getItems();
        ArrayList<String> productName = new ArrayList<>();

        for(int i=0; i<itemList.size(); i++) {
            Items.Item item = Items.getItems().get(i);
            String itemVal = item.productName;
            productName.add(itemVal);
        }

        myListAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, productName);

        itemListView.setAdapter(myListAdapter);
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
        swiperefresh.setOnRefreshListener(() -> {
            Database.updateData();
            myListAdapter.notifyDataSetChanged();
            swiperefresh.setRefreshing(false);
        });

    }

}