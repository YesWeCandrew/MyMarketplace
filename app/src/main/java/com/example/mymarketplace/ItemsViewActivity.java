package com.example.mymarketplace;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mymarketplace.Entities.Database;
import com.example.mymarketplace.Entities.Reviews;
import com.example.mymarketplace.Helpers.CSVReader;
import com.example.mymarketplace.Entities.Items;
import com.example.mymarketplace.Entities.Sellers;
import com.example.mymarketplace.Entities.Stocks;
import com.example.mymarketplace.Entities.Users;
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
 * @author: Andrew Howes, Vincent Tanumihardja, Matthew Cawley
 */
public class ItemsViewActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private SwipeRefreshLayout swiperefresh;
    private ArrayAdapter myListAdapter;
    private EditText searchBox;
    private Button searchButton;
    private ListView itemListView;
    private Spinner sortByMenu;
    private String[] sortByTypes = {"Price (Low to High)", "Price (High to Low)", "Name"};
    private Users.User user;
    private ArrayList<Items.Item> currDisplayedItems; //the current list of items the view is displaying

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_view);

        searchBox = (EditText) findViewById(R.id.editTextTextPersonName2);
        searchBox.setText("");
        searchButton = (Button) findViewById(R.id.button);
        searchButton.setText("Search");
        searchButton.setOnClickListener(searchButtonPress);
        user = getIntent().getSerializableExtra("user", Users.User.class);

        //setup spinner (the drop down menu for sort type)
        sortByMenu = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter aa = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, sortByTypes);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortByMenu.setAdapter(aa);
        sortByMenu.setOnItemClickListener((AdapterView.OnItemClickListener) this);

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

        // Display User's name
        ((TextView)findViewById(R.id.name)).setText(user.givenName);

        // Getting ImageView for the user profile picture
        ImageView sellerImageView = findViewById(R.id.userImageView);

        // Getting photo directory of the user
        int userPhotoDir = getResources().getIdentifier(user.photoDirectory,"drawable", getPackageName());

        // Set the user image
        sellerImageView.setImageResource(userPhotoDir);

        //ListView of all the product names
        itemListView = (ListView) findViewById(R.id.itemsListView);
        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        ArrayList<Items.Item> itemList = Items.getItems();
        currDisplayedItems = itemList;
        updateListView(currDisplayedItems);
    }

    /** Updates the displayed items list to instead display a new list of items
     * @param items list of items to be displayed
     */
    public void updateListView(ArrayList<Items.Item> items){
        ArrayList<String> productName = new ArrayList<>();

        for(Items.Item i : items) {
            String itemVal = i.productName;
            productName.add(itemVal);
        }

        myListAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, productName);

        itemListView.setAdapter(myListAdapter);
        itemListView.setDivider(null);
        itemListView.setClickable(true);
        // only proceed to item info when the user is logged in
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (user.loggedIn) {
                    Intent intent = new Intent(ItemsViewActivity.this, ItemInfo.class);
                    intent.putExtra("item", Items.getItems().get(position));
                    startActivity(intent);
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "You are not logged in. Please login to continue.", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        swiperefresh.setOnRefreshListener(() -> {
            Database.updateData();
            myListAdapter.notifyDataSetChanged();
            swiperefresh.setRefreshing(false);
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
            updateListView(currDisplayedItems);
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
            updateListView(currDisplayedItems);
        } else if (position == 1) { //by price, high to low
            currDisplayedItems = Items.getInstance().sortByPrice(currDisplayedItems);
            Collections.reverse(currDisplayedItems);
            updateListView(currDisplayedItems);
        } else if (position == 2) { //by name
            currDisplayedItems = Items.getInstance().sortByName(currDisplayedItems);
            updateListView(currDisplayedItems);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}