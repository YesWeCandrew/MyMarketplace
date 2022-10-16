package com.example.mymarketplace;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mymarketplace.Entities.Items;
import com.example.mymarketplace.Entities.Sellers;

public class ItemInfo extends AppCompatActivity {

    private Items.Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        item = getIntent().getSerializableExtra("item", Items.Item.class);

        // Getting the TextViews of the item
        ImageView itemImageView = findViewById(R.id.itemImageView);
        TextView textViewProductName = findViewById(R.id.textViewProductName);
        Button buttonLearnMoreSeller = findViewById(R.id.buttonLearnMoreSeller);
        Button buttonPayment = findViewById(R.id.payment);
        TextView textViewDescription = findViewById(R.id.textViewDescription);
        TextView textPricing = findViewById(R.id.textPricing);
        TextView textReviews = findViewById(R.id.textViewReviews);
        TextView textStock = findViewById(R.id.textViewStock);

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
        textReviews.setText(item.averageRatingAsText);

        // Setting Seller button listener
        buttonLearnMoreSeller.setOnClickListener(sellerButtonListener);
        buttonPayment.setOnClickListener(paymentButtonListener);

        // Checking if item is a scam
        if (ScamChecker.isScam(item)) {

            // If there is scam show a warning to the user, but still let them proceed.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(R.string.ScamWarningMessage)
                    .setTitle(R.string.ScamWarningTitle);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    // Listener for the payment button
    private final View.OnClickListener paymentButtonListener = new View.OnClickListener() {
        @Override
        public void onClick (View view) {
            // start a new activity, which is making a payment, when the user decides to buy a single item.
            Intent intent = new Intent(ItemInfo.this, PaymentActivity.class);
            intent.putExtra("item", item);
            startActivity(intent);
        }
    };

    // Listener for the seller button
    private final View.OnClickListener sellerButtonListener = new View.OnClickListener() {
        @Override
        public void onClick (View view) {
            // start a new activity, which is searching through the marketplace, when the credentials are valid.
            Intent intent = new Intent(ItemInfo.this, SellerInfo.class);
            intent.putExtra("seller", Sellers.getSellers().get(item.sellerID));
            startActivity(intent);
        }
    };
}