package com.example.mymarketplace.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

        // Change Colour of Action Bar & Status Bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ocean)));
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.ocean));
        setContentView(R.layout.activity_order_confirmation);

        Button button = findViewById(R.id.buttonBuyAgain);

        // Setting Button Colors
        button.setBackgroundColor(getResources().getColor(R.color.darkgrey));

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