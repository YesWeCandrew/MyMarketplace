package com.example.mymarketplace.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mymarketplace.R;

/**
 * This activity creates a registration screen for the user
 * Saves the user information for login validation (currently not implemented)
 * @author: Vincent Tanumihardja
 * References:
 * - Code Structure: Week 7 Lecture Login Activity
 * - Background image: https://wallpaperaccess.com/android-gradient
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText et_gender;
    private EditText et_title;
    private EditText et_name;
    private EditText et_surname;
    private EditText et_state;
    private EditText et_zip;
    private EditText et_username;
    private EditText et_password;
    private Button button_register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setting the content of the register activity
        setContentView(R.layout.activity_register);

        // Change Colour of Action Bar & Status Bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ocean)));
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.ocean));

        // get the views by their id
        EditText et_gender = (EditText) findViewById(R.id.gender);
        EditText et_title = (EditText) findViewById(R.id.title);
        EditText et_name = (EditText) findViewById(R.id.name);
        EditText et_surname = (EditText) findViewById(R.id.surname);
        EditText et_state = (EditText) findViewById(R.id.state);
        EditText et_zip = (EditText) findViewById(R.id.zip);
        EditText et_username = (EditText) findViewById(R.id.username2);
        EditText et_password = (EditText) findViewById(R.id.password2);
        Button button_register = (Button) findViewById(R.id.register2);
        button_register.setOnClickListener(buttonListener);

        // Setting Button Colors
        button_register.setBackgroundColor(getResources().getColor(R.color.darkgrey));

        Log.i(RegisterActivity.class.getName(), "created.");
    }

    /**
     * This method is called when the activity is visible to the user.
     */
    @Override
    protected void onStart() {
        super.onStart();

        // clear all values in username and password edit text box when the activity starts
        EditText et_gender = (EditText) findViewById(R.id.gender);
        EditText et_title = (EditText) findViewById(R.id.title);
        EditText et_name = (EditText) findViewById(R.id.name);
        EditText et_surname = (EditText) findViewById(R.id.surname);
        EditText et_state = (EditText) findViewById(R.id.state);
        EditText et_zip = (EditText) findViewById(R.id.zip);
        EditText et_username = (EditText) findViewById(R.id.username2);
        EditText et_password = (EditText) findViewById(R.id.password2);
        et_gender.requestFocus();
        et_gender.setText("");
        et_title.setText("");
        et_name.setText("");
        et_surname.setText("");
        et_state.setText("");
        et_zip.setText("");
        et_username.setText("");
        et_password.setText("");

        Log.i(RegisterActivity.class.getName(), "started.");
    }

    /**
     * This method resumes the previously running activity when it comes into the foreground.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(RegisterActivity.class.getName(), "resumed.");
    }

    /**
     * This method restarts the activity when it comes back from the background.
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(RegisterActivity.class.getName(), "restarted.");
    }

    /**
     * This method pauses the activity when the user leaves the activity.
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(RegisterActivity.class.getName(), "paused.");
    }

    /**
     * This method stops the activity when it is not visible to the user.
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(RegisterActivity.class.getName(), "stopped.");
    }

    /**
     * This method destroys the activity.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(RegisterActivity.class.getName(), "destroyed.");
    }

    /**
     * Creates a button click listener for the buttons
     * If the button clicked is the register button, go to the user registration page
     * Else check whether login credentials are valid
     */
    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick (View view) {

            // get the string text from the text editors
            String gender = et_gender.getText().toString();
            String title = et_title.getText().toString();
            String name = et_name.getText().toString();
            String surname = et_surname.getText().toString();
            String state = et_state.getText().toString();
            String zip = et_zip.getText().toString();
            String username = et_username.getText().toString();
            String password = et_password.getText().toString();

            // check the validity of the inputs
            WarningResult warningResult = checkInputsAndGetWarning(username, password);

            // show a warning message if the inputs are empty.
            if (warningResult.isInvalid) {
                showWarning(warningResult.text);
                return;
            }

            // get the resources object
            Resources resources = getResources();

            // start a new activity, which is searching through the marketplace, when the credentials are valid.
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    };

    /**
     * Define a warning result object which is returned by checkInputsAndGetWarning() method
     */
    private static class WarningResult {
        private boolean isInvalid;
        private String text;

        public WarningResult(boolean isInvalid, String text){
            this.isInvalid = isInvalid;
            this.text = text;
        }
    }

    /**
     * Show a toast warning based on the given input text
     * @param text
     */
    private void showWarning(String text) {
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * Check whether the string inputs are valid and return the first input that is invalid
     * @param username
     * @param password
     * @return a WarningResult when input is empty
     */
    private RegisterActivity.WarningResult checkInputsAndGetWarning(String username, String password) {

        Resources resources = getResources();
        boolean isInvalid = false;
        String warning = "";

        if (isEmpty(username)) {
            warning = resources.getString(R.string.warning_empty_credential, resources.getString(R.string.username));
            isInvalid = true;
        }
        else if (isEmpty(password)) {
            warning = resources.getString(R.string.warning_empty_credential, resources.getString(R.string.password));
            isInvalid = true;
        }

        return new WarningResult(isInvalid, warning);
    }

    /**
     * Check whether a string is empty or null
     * @param text
     * @return true or false
     */
    private boolean isEmpty(String text){
        return text == null || text.length() <= 0;
    }
}