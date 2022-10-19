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

/**
 * This activity creates a list of items sold on the marketplace for the user
 * This class implements state design pattern where only logged in users may proceed to view item details
 * @author: Andrew Howes, Vincent Tanumihardja, Matthew Cawley, Long Vu
 */
public class ItemsViewActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private SwipeRefreshLayout swipeRefresh;
    private EditText searchBox;
    private Button searchButton;
    private ListView itemListView;
    private Spinner sortByMenu;
    private String[] sortByTypes = {"Price (Low to High)", "Price (High to Low)", "Name"};
    private Users.User user;
    private ArrayList<Items.Item> currDisplayedItems; //the current list of items the view is displaying
    private static CustomListViewAdapter adapter;

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

        searchBox = (EditText) findViewById(R.id.editTextSearch);
        searchBox.setText("");
        searchButton = (Button) findViewById(R.id.button);
        searchButton.setOnClickListener(searchButtonPress);
        user = getIntent().getSerializableExtra("user", Users.User.class);

        //setup spinner (the drop down menu for sort type)
        sortByMenu = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter aa = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, sortByTypes);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortByMenu.setAdapter(aa);
//        sortByMenu.setOnItemClickListener((AdapterView.OnItemClickListener) this);

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


        /**
         * Displaying Products in ListView
         */

        ArrayList<Items.Item> itemList = Items.getItems(); // List of Items
        //ListView of all the product names
        itemListView = (ListView) findViewById(R.id.itemsListView);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        currDisplayedItems = itemList;

        adapter = new CustomListViewAdapter(currDisplayedItems,getApplicationContext());

        itemListView.setAdapter(adapter);
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Items.Item item = currDisplayedItems.get(position);

            }
        });

        // Swipe Refresh
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ItemsViewActivity.this, ItemInfo.class);
                intent.putExtra("item", Items.getItems().get(position));
                startActivity(intent);
            }
        });
        swipeRefresh.setOnRefreshListener(() -> {
            Database.updateData();
            adapter.notifyDataSetChanged();
            swipeRefresh.setRefreshing(false);
        });
    }

    /**
     * Called when the search button is pressed
     */
    private View.OnClickListener searchButtonPress = new View.OnClickListener() {
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
                    for(Items.Item i : resultItems){
                        if(!i.sellerName.equals(t.getToken())){
                            resultItems.remove(i);
                        }
                    }
                }if(t.getType() == Token.Type.CAT){
                    for(Items.Item i : resultItems){
                        if(!i.category.equals(t.getToken())){
                            resultItems.remove(i);
                        }
                    }
                }if(t.getType() == Token.Type.SUBCAT){
                    for(Items.Item i : resultItems){
                        if(!i.subcategory.equals(t.getToken())){
                            resultItems.remove(i);
                        }
                    }
                }if(t.getType() == Token.Type.PRICEMAX){
                    for(Items.Item i : resultItems){
                        if(i.price >= Integer.parseInt(t.getToken())){
                            resultItems.remove(i);
                        }
                    }
                }if(t.getType() == Token.Type.PRICEMIN){
                    for(Items.Item i : resultItems){
                        if(i.price <= Integer.parseInt(t.getToken())){
                            resultItems.remove(i);
                        }
                    }
                }
            }
            currDisplayedItems = resultItems;
            adapter.notifyDataSetChanged();
        }
    };

    /**
     * Called when one of the options in the drop-down menu for the sort by button is pressed.
     * @param position the button that was pressed
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) { //by price, low to high
            currDisplayedItems = Items.getInstance().sortByPrice(currDisplayedItems);
            adapter.notifyDataSetChanged();
        } else if (position == 1) { //by price, high to low
            currDisplayedItems = Items.getInstance().sortByPrice(currDisplayedItems);
            Collections.reverse(currDisplayedItems);
            adapter.notifyDataSetChanged();
        } else if (position == 2) { //by name
            currDisplayedItems = Items.getInstance().sortByName(currDisplayedItems);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}