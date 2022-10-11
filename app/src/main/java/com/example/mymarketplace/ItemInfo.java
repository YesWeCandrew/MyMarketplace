package com.example.mymarketplace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mymarketplace.Entities.Items;
import com.example.mymarketplace.Entities.Users;

public class ItemInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        Items.Item item = getIntent().getSerializableExtra("item", Items.Item.class);

        ImageView itemImageView = (ImageView) findViewById(R.id.itemImageView);
        TextView textViewProductName = (TextView) findViewById(R.id.textViewProductName);
        Button buttonLearnMoreSeller = (Button) findViewById(R.id.buttonLearnMoreSeller);
        TextView textViewDescription = (TextView) findViewById(R.id.textViewDescription);
        TextView textPricing = (TextView) findViewById(R.id.textPricing);
        // Getting photo dir:
        int itemPhotoDir = getResources().getIdentifier(item.photoDirectory,"drawable", getPackageName());

        // Setting values
        itemImageView.setImageResource(itemPhotoDir);
        textViewProductName.setText(item.productName);
        buttonLearnMoreSeller.setText(item.sellerName);
        textViewDescription.setText(item.description);
        textPricing.setText("$" + item.price+ ".00");
    }
}