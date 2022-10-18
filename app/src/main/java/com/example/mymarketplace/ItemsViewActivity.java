package com.example.mymarketplace;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mymarketplace.Entities.Reviews;
import com.example.mymarketplace.Helpers.CSVReader;
import com.example.mymarketplace.Entities.Items;
import com.example.mymarketplace.Entities.Sellers;
import com.example.mymarketplace.Entities.Stocks;
import com.example.mymarketplace.Entities.Users;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ItemsViewActivity extends AppCompatActivity {

    private SwipeRefreshLayout swiperefresh;
    private ArrayAdapter myListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_view);


        // Change Colour of Action Bar & Status Bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ocean)));
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.ocean));


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

            // Inputting
            is = am.open("Reviews.csv");
            Reviews.reviewsFromCSV(CSVReader.parseCsv(is));

            // Getting the first batch of stock data and updating item stock
            Stocks.addBatch();
            Reviews.addBatch();

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
         * Displaying Products in ListView
         */

        ArrayList<Items.Item> itemList = Items.getItems(); // List of Items

        int[] images = {R.drawable.item0, R.drawable.item1, R.drawable.item2, R.drawable.item3, R.drawable.item4, R.drawable.item5,
                R.drawable.item6, R.drawable.item7, R.drawable.item8, R.drawable.item9, R.drawable.item10, R.drawable.item11,
                R.drawable.item12, R.drawable.item13,R.drawable.item14, R.drawable.item15, R.drawable.item16, R.drawable.item17,
                R.drawable.item18, R.drawable.item19, R.drawable.item20, R.drawable.item21, R.drawable.item22, R.drawable.item23,
                R.drawable.item24, R.drawable.item25, R.drawable.item26, R.drawable.item27, R.drawable.item28, R.drawable.item29,
                R.drawable.item30, R.drawable.item31, R.drawable.item32, R.drawable.item33, R.drawable.item34, R.drawable.item35,
                R.drawable.item36, R.drawable.item37, R.drawable.item38, R.drawable.item39, R.drawable.item40, R.drawable.item41,
                R.drawable.item42, R.drawable.item43, R.drawable.item44, R.drawable.item45, R.drawable.item46, R.drawable.item47,
                R.drawable.item48, R.drawable.item49};

        ArrayList<String> itemName = new ArrayList<>();
        ArrayList<String> sellerName = new ArrayList<>();
        ArrayList<String> averageRating = new ArrayList<>();

        for(int i=0; i<itemList.size(); i++) {
            Items.Item item = Items.getItems().get(i);

            String itemName2 = item.productName;
            String sellerName2 = item.sellerName;
            String averageRating2 = item.averageRatingAsText;

            itemName.add(itemName2);
            sellerName.add(sellerName2);
            averageRating.add(averageRating2);
        }

        ListView itemListView = (ListView) findViewById(R.id.itemsListView);

        CustomListViewAdapter customListViewAdapter = new CustomListViewAdapter(this, images, itemName, sellerName, averageRating);
        itemListView.setAdapter(customListViewAdapter);

        // Swipe Refresh
        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ItemsViewActivity.this, ItemInfo.class);
                intent.putExtra("item",Items.getItems().get(position));
                startActivity(intent);
            }
        });
        swiperefresh.setOnRefreshListener(() -> {
            Stocks.addBatch();
            Reviews.addBatch();
            myListAdapter.notifyDataSetChanged();
            swiperefresh.setRefreshing(false);
        });

    }

}