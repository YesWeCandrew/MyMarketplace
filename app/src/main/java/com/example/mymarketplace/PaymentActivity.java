package com.example.mymarketplace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mymarketplace.Entities.Items;

/**
 * This activity creates a payment page where user can choose the preferred payment method to buy the item
 * Items paid by card will have a 0.5% surcharge and paypal have a 3% surcharge
 * @author: Vincent Tanumihardja
 */
public class PaymentActivity extends AppCompatActivity {

    private Items.Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        item = getIntent().getSerializableExtra("item", Items.Item.class);

        // Getting the TextViews of the item
        TextView itemSummary = findViewById(R.id.summary);
        ImageView itemImageView = findViewById(R.id.itemImageView2);
        TextView itemName = findViewById(R.id.itemName);
        TextView cash = findViewById(R.id.textViewCash);
        TextView cashCost = findViewById(R.id.cashCost);
        Button buttonCash = findViewById(R.id.cash);
        TextView card = findViewById(R.id.textViewCard);
        TextView cardCost = findViewById(R.id.cardCost);
        Button buttonCard = findViewById(R.id.card);
        TextView paypal = findViewById(R.id.textViewPaypal);
        TextView paypalCost = findViewById(R.id.paypalCost);
        Button buttonPaypal = findViewById(R.id.paypal);

        // Getting photo directory
        int itemPhotoDir = getResources().getIdentifier(item.photoDirectory,"drawable", getPackageName());

        // Setting values
        itemImageView.setImageResource(itemPhotoDir);
        itemName.setText(item.productName);
        cashCost.setText(item.priceAsText);
        cardCost.setText("$" + String.format("%.2f",item.price * 1.005));
        paypalCost.setText("$" + String.format("%.2f",item.price * 1.03));

        // setting button listeners
        buttonCash.setOnClickListener(paymentButtonListener);
        buttonCard.setOnClickListener(paymentButtonListener);
        buttonPaypal .setOnClickListener(paymentButtonListener);

    }

    private final View.OnClickListener paymentButtonListener = new View.OnClickListener() {
        @Override
        public void onClick (View view) {
            // start a new activity, which is making a payment, when the user decides to buy a single item.
            Intent intent = new Intent(PaymentActivity.this, OrderConfirmationActivity.class);
            startActivity(intent);
        }
    };
}