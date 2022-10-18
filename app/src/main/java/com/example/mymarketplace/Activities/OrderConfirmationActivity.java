package com.example.mymarketplace.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mymarketplace.R;

/**
 * This activity creates order confirmation page
 * User can click on buy again item to purchase the same item again
 * @author: Vincent Tanumihardja
 * References:
 * - Tick image: https://blog.stylingandroid.com/animatedicons-radio-button/
 */
public class OrderConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        Button button = findViewById(R.id.buttonBuyAgain);

        button.setOnClickListener(returnButtonListener);
    }

    // Listener for the return button
    private final View.OnClickListener returnButtonListener = new View.OnClickListener() {
        @Override
        public void onClick (View view) {
            // return to item view activity
            Intent intent = new Intent(OrderConfirmationActivity.this, ItemInfo.class);
            startActivity(intent);
        }
    };

}