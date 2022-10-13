package com.example.mymarketplace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mymarketplace.Entities.Items;
import com.example.mymarketplace.Entities.Sellers;
import com.example.mymarketplace.Entities.Stocks;
import com.example.mymarketplace.Entities.Users;
import com.example.mymarketplace.Helpers.Hasher;

public class ItemInfo extends AppCompatActivity {

    private Items.Item item;
    private TextView textStock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        item = getIntent().getSerializableExtra("item", Items.Item.class);

        ImageView itemImageView = (ImageView) findViewById(R.id.itemImageView);
        TextView textViewProductName = (TextView) findViewById(R.id.textViewProductName);
        Button buttonLearnMoreSeller = (Button) findViewById(R.id.buttonLearnMoreSeller);
        TextView textViewDescription = (TextView) findViewById(R.id.textViewDescription);
        TextView textPricing = (TextView) findViewById(R.id.textPricing);
        textStock = (TextView) findViewById(R.id.textViewStock);

        // Getting photo dir:
        int itemPhotoDir = getResources().getIdentifier(item.photoDirectory,"drawable", getPackageName());

        String sellerButtonText = "Sold by: " + item.sellerName;

        // Setting values
        itemImageView.setImageResource(itemPhotoDir);
        textViewProductName.setText(item.productName);
        buttonLearnMoreSeller.setText(sellerButtonText);
        textViewDescription.setText(item.description);
        textPricing.setText(item.priceAsText);
        textStock.setText(item.quantityAsText);

        // Setting Seller button listener
        Button buttonSeller = (Button) findViewById(R.id.buttonLearnMoreSeller);
        buttonSeller.setOnClickListener(sellerButtonListener);
    }

    // Listener for the seller button
    private View.OnClickListener sellerButtonListener = new View.OnClickListener() {
        @Override
        public void onClick (View view) {
            // start a new activity, which is searching through the marketplace, when the credentials are valid.
            Intent intent = new Intent(ItemInfo.this, SellerInfo.class);
            intent.putExtra("seller", Sellers.getSellers().get(item.sellerID));
            startActivity(intent);
        }
    };
}