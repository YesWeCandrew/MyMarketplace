package com.example.mymarketplace.Activities;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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

import com.example.mymarketplace.Entities.Sellers;
import com.example.mymarketplace.Helpers.CustomListViewAdapter;
import com.example.mymarketplace.Entities.Database;
import com.example.mymarketplace.Entities.Items;
import com.example.mymarketplace.Entities.Users;
import com.example.mymarketplace.Helpers.LoginState;
import com.example.mymarketplace.R;
import com.example.mymarketplace.Search.AVLTree;
import com.example.mymarketplace.Search.Node;
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
    private Button clearButton;
    private ListView itemListView;
    private Spinner sortByMenu;
    private final String[] sortByTypes = {"Price (Low to High)", "Price (High to Low)", "Reviews (High to Low)", "Name"};
    private Users.User user;
    private ArrayList<Items.Item> currDisplayedItems; //the current list of items the view is displaying
    private ArrayList<Items.Item> allItems; //the list of all items the view is displaying
    private CustomListViewAdapter adapter;
    private AVLTree avlTree;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_view);
        avlTree = new AVLTree();

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

        // Load all assets to the correct classes
        AssetManager am = this.getAssets();
        try {
            // Inputting items
            InputStream is = am.open("Items.csv");
            if (Items.getItems().size() == 0) {
                Database.importData(is, Database.DataType.Items, avlTree);
            }

            // Inputting sellers
            is = am.open("Sellers.csv");
            if (Sellers.getSellers().size() == 0) {
                Database.importData(is, Database.DataType.Sellers, avlTree);
            }

            // Inputting stock
            is = am.open("Stock.csv");
            Database.importData(is, Database.DataType.Stock, avlTree);

            // Inputting
            is = am.open("Reviews.csv");
            Database.importData(is, Database.DataType.Reviews, avlTree);

            // Getting the first two batches of stock data and updating item stock
            Database.updateData();
            Database.updateData();

            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Finding by ID
        searchBox = findViewById(R.id.editTextSearch);
        searchButton = findViewById(R.id.button);
        clearButton = findViewById(R.id.clearButton);
        sortByMenu = findViewById(R.id.spinner);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        ImageView sellerImageView = findViewById(R.id.userImageView);
        itemListView =  findViewById(R.id.itemsListView);

        // Setting initial values
        searchBox.setText("");

        // Setting on click listeners
        searchButton.setOnClickListener(searchButtonPress);
        clearButton.setOnClickListener(clearButtonPress);

        //setup spinner (the drop down menu for sort type)
        ArrayAdapter sortByAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, sortByTypes);
        sortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortByMenu.setAdapter(sortByAdapter);
        sortByMenu.setOnItemSelectedListener(sortByClicked);

        // Setting the user image
        int userPhotoDir = getResources().getIdentifier(user.photoDirectory,"drawable", getPackageName());
        sellerImageView.setImageResource(userPhotoDir);

        // Displaying items
        allItems = new ArrayList<Items.Item>();
        allItems.addAll(Items.getItems());

        currDisplayedItems = new ArrayList<Items.Item>();
        currDisplayedItems.addAll(Items.getItems());

        // Sort by price high to low by default
        currDisplayedItems.sort((Comparator.comparingInt(item1 -> item1.price)));
        Collections.reverse(currDisplayedItems);

        adapter = new CustomListViewAdapter(currDisplayedItems,getApplicationContext());
        itemListView.setAdapter(adapter);
        itemListView.setOnItemClickListener(itemClicked);

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
            String searchTerm = searchBox.getText().toString();

            ArrayList<Token> searchTokens;
            Tokenizer tokenizer = new Tokenizer();
            searchTokens = tokenizer.Tokenize(searchTerm);

            if(searchTokens.get(0).getType() == Token.Type.NULL){
                if(searchTokens.get(0).getToken() == "noColonError"){
                    Toast toast = Toast.makeText(getApplicationContext(), "Missing colon in search term", Toast.LENGTH_LONG);
                    toast.show();
                }
                else if(searchTokens.get(0).getToken() == "incorrectSearchType"){
                    Toast toast = Toast.makeText(getApplicationContext(), "Invalid Search Term", Toast.LENGTH_LONG);
                    toast.show();
                }
                return;
            }
            if(searchTokens.size() < 1){
                Toast toast = Toast.makeText(getApplicationContext(), "No valid search terms found", Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            for (Token t : searchTokens) { //look for a product name token first
                if (t.getType() == Token.Type.PNAME) {
                    currDisplayedItems.clear();
                    Node result = avlTree.search(t.getToken());
                    if(result == null){
                        Toast toast = Toast.makeText(getApplicationContext(), "No results found", Toast.LENGTH_LONG);
                        toast.show();
                    }else {
                        currDisplayedItems.add(avlTree.search(t.getToken()).getItem());
                    }
                }
            }
            for (Token t : searchTokens){ //then go through the rest of the tokens
                if(t.getType() == Token.Type.PNAME){
                    currDisplayedItems.removeIf(i -> !i.productName.equals(t.getToken()));
                }if(t.getType() == Token.Type.SNAME){
                    currDisplayedItems.removeIf(i -> !i.sellerName.equals(t.getToken()));
                }if(t.getType() == Token.Type.CAT){
                    currDisplayedItems.removeIf(i -> !i.category.equals(t.getToken()));
                }if(t.getType() == Token.Type.SUBCAT){
                    currDisplayedItems.removeIf(i -> !i.subcategory.equals(t.getToken()));
                }if(t.getType() == Token.Type.PRICEMAX){
                    currDisplayedItems.removeIf(i -> i.price >= Integer.parseInt(t.getToken()));
                }if(t.getType() == Token.Type.PRICEMIN){
                    currDisplayedItems.removeIf(i -> i.price <= Integer.parseInt(t.getToken()));
                }
            }
            Log.i("number of result items", String.valueOf(currDisplayedItems.size()));
            adapter.notifyDataSetChanged();
        }
    };

    /**
     * Called when the clear button is pressed. sets currDisplayedItems back to all items.
     */
    private final View.OnClickListener clearButtonPress = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            currDisplayedItems = new ArrayList<>();
            currDisplayedItems.addAll(allItems);
            adapter = new CustomListViewAdapter(currDisplayedItems,getApplicationContext());
            itemListView.setAdapter(adapter);
            searchBox.setText("");
        }
    };
}
