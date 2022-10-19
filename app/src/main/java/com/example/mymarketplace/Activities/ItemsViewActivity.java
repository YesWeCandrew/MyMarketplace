package com.example.mymarketplace.Activities;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mymarketplace.Helpers.CustomListViewAdapter;
import com.example.mymarketplace.Entities.Database;
import com.example.mymarketplace.Entities.Items;
import com.example.mymarketplace.Entities.Users;
import com.example.mymarketplace.Helpers.LoginState;
import com.example.mymarketplace.R;
import com.example.mymarketplace.Search.AVLTree;
import com.example.mymarketplace.Search.Token;
import com.example.mymarketplace.Search.Tokenizer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * This activity creates a list of items sold on the marketplace for the user
 * This class implements state design pattern where only logged in users may proceed to view item details
 * @author: Andrew Howes, Vincent Tanumihardja, Matthew Cawley, Long Vu
 */
public class ItemsViewActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefresh;
    private EditText searchBox;
    private Button searchButton;
    private ListView itemListView;
    private Spinner sortByMenu;
    private final String[] sortByTypes = {"Price (Low to High)", "Price (High to Low)", "Reviews (High to Low)", "Name"};
    private Users.User user;
    private ArrayList<Items.Item> currDisplayedItems; //the current list of items the view is displaying
    private CustomListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_view);

        // Getting the user and ensuring state is successful
        user = getIntent().getSerializableExtra("user", Users.User.class);
        LoginState state = getIntent().getSerializableExtra("state", LoginState.class);

        // Checking the application state
        if (!(state.equals(LoginState.SUCCESS))) {
            Toast toast = Toast.makeText(getApplicationContext(), "You are not logged in!", Toast.LENGTH_LONG);
            toast.show();
            finish();
        } else {
            // Display User's name
            Toast toast = Toast.makeText(getApplicationContext(), "Hi, " + user.givenName, Toast.LENGTH_LONG);
            toast.show();
        }

        // Change Colour of Action Bar & Status Bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ocean)));
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.ocean));

        searchBox = findViewById(R.id.editTextSearch);
        searchBox.setText("");
        searchButton = findViewById(R.id.button);
        searchButton.setOnClickListener(searchButtonPress);
        user = getIntent().getSerializableExtra("user", Users.User.class);

        //setup spinner (the drop down menu for sort type)
        sortByMenu = findViewById(R.id.spinner);
        ArrayAdapter sortByAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, sortByTypes);
        sortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortByMenu.setAdapter(sortByAdapter);
        sortByMenu.setOnItemSelectedListener(sortByClicked);

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

            // Getting the first two batches of stock data and updating item stock
            Database.updateData();
            Database.updateData();

            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Getting ImageView for the user profile picture
        ImageView sellerImageView = findViewById(R.id.userImageView);

        // Getting photo directory of the user
        int userPhotoDir = getResources().getIdentifier(user.photoDirectory,"drawable", getPackageName());

        // Set the user image
        sellerImageView.setImageResource(userPhotoDir);

        ArrayList<Items.Item> itemList = Items.getItems(); // List of Items
        //ListView of all the product names
        itemListView =  findViewById(R.id.itemsListView);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        currDisplayedItems = itemList;
        // Sort by price high to low by default
        currDisplayedItems.sort((Comparator.comparingInt(item1 -> item1.price)));
        Collections.reverse(currDisplayedItems);

        adapter = new CustomListViewAdapter(currDisplayedItems,getApplicationContext());

        itemListView.setAdapter(adapter);
        itemListView.setOnItemClickListener(itemClicked);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(() -> {
            Database.updateData();
            adapter.notifyDataSetChanged();
            swipeRefresh.setRefreshing(false);
        });

    }



    private final AdapterView.OnItemClickListener itemClicked = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(ItemsViewActivity.this, ItemInfo.class);
            intent.putExtra("item", currDisplayedItems.get(position));
            startActivity(intent);
        }
    };


    private final AdapterView.OnItemSelectedListener sortByClicked = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) { //by price, low to high
                currDisplayedItems.sort((Comparator.comparingInt(item1 -> item1.price)));
                adapter.notifyDataSetChanged();
            } else if (position == 1) { //by price, high to low
                currDisplayedItems.sort((Comparator.comparingInt(item1 -> item1.price)));
                Collections.reverse(currDisplayedItems);
                adapter.notifyDataSetChanged();
            } else if (position == 2) { //by reviews, high to low
                currDisplayedItems.sort((Comparator.comparingDouble(item1 -> item1.averageRating)));
                Collections.reverse(currDisplayedItems);
                adapter.notifyDataSetChanged();
            }
            else { //by name
                currDisplayedItems.sort((Comparator.comparing(item1 -> item1.productName)));
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    /**
     * Called when the search button is pressed
     */
    private final View.OnClickListener searchButtonPress = new View.OnClickListener() {
        @Override
        public void onClick (View view) {
            ArrayList<Items.Item> resultItems = Items.getItems();

            String searchTerm = searchBox.getText().toString();

            ArrayList<Token> searchTokens;
            Tokenizer tokenizer = new Tokenizer(searchTerm);
            searchTokens = tokenizer.getTokens();

            boolean hasPName = false;

            if(searchTokens.size() < 1){
                return;
            }

            for (Token t : searchTokens) { //look for a product name token first
                if (t.getType() == Token.Type.PNAME) {
                    resultItems.add(AVLTree.search(t.getToken()).getItem());
                    hasPName = true;
                }
            }
            if(!hasPName) {
                resultItems = Items.getItems(); //if they don't provide a product name, have to search through all
            }
            for (Token t : searchTokens){ //then go through the rest of the tokens
                if(t.getType() == Token.Type.SNAME){
                    resultItems.removeIf(i -> !i.sellerName.equals(t.getToken()));
                }if(t.getType() == Token.Type.CAT){
                    resultItems.removeIf(i -> !i.category.equals(t.getToken()));
                }if(t.getType() == Token.Type.SUBCAT){
                    resultItems.removeIf(i -> !i.subcategory.equals(t.getToken()));
                }if(t.getType() == Token.Type.PRICEMAX){
                    resultItems.removeIf(i -> i.price >= Integer.parseInt(t.getToken()));
                }if(t.getType() == Token.Type.PRICEMIN){
                    resultItems.removeIf(i -> i.price <= Integer.parseInt(t.getToken()));
                }
            }
            currDisplayedItems = resultItems;
            adapter.notifyDataSetChanged();
        }
    };
}