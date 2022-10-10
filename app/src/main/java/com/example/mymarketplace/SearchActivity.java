package com.example.mymarketplace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.EditText;

import com.example.mymarketplace.Entities.AVLTree;
import com.example.mymarketplace.Entities.Items;
import com.example.mymarketplace.Entities.Token;
import com.example.mymarketplace.Entities.Tokenizer;

import java.util.ArrayList;

/** This activity provides a search box through which
 *  users can search the items on the store.
 *  @author: Matthew Cawley
 */
public class SearchActivity extends AppCompatActivity {

    private EditText searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LoginActivity.class.getName(), "started.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LoginActivity.class.getName(), "resumed.");
    }

    /**
     * This method restarts the activity when it comes back from the background.
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LoginActivity.class.getName(), "restarted.");
    }

    /**
     * This method pauses the activity when the user leaves the activity.
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LoginActivity.class.getName(), "paused.");
    }

    /**
     * This method stops the activity when it is not visible to the user.
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LoginActivity.class.getName(), "stopped.");
    }

    /**
     * This method destroys the activity.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LoginActivity.class.getName(), "destroyed.");
    }

    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick (View view) {

            ArrayList<Items.Item> resultItems = new ArrayList<Items.Item>();

            // get the string text from the text editors
            String searchTerm = searchBox.getText().toString();

            ArrayList<Token> searchTokens = new ArrayList<Token>();
            Tokenizer tokenizer = new Tokenizer(searchTerm);
            searchTokens = tokenizer.tokens;

            boolean hasPName = false;

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

            //the final items are now held in resultItems

            // might have to restart the items view activity??
          //  Intent intent = new Intent(LoginActivity.this, ItemsViewActivity.class);
          //  intent.putExtra("userID",userID);
          //  startActivity(intent);
        }
    };
}