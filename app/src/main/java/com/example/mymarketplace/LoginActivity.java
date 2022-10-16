package com.example.mymarketplace;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mymarketplace.Helpers.CSVReader;
import com.example.mymarketplace.Entities.Users;
import com.example.mymarketplace.Helpers.Hasher;

import java.io.IOException;
import java.io.InputStream;
/**
 * This activity creates a login screen for the user
 * Only valid users may proceed to the marketplace
 * @author: Vincent Tanumihardja
 * References:
 * - Code Structure: Week 7 Lecture Login Activity
 * - Background image: https://wallpaperaccess.com/android-gradient
 */
public class LoginActivity extends AppCompatActivity {

    private EditText et_username;
    private EditText et_password;
    private Button button_login;
    private Button button_register;

    /**
     * This method creates the login screen activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setting the contents of login
        setContentView(R.layout.activity_login);

        // get the views by their id
        EditText et_username = (EditText) findViewById(R.id.username);
        EditText et_password = (EditText) findViewById(R.id.password);
        Button button_login = (Button) findViewById(R.id.login);
        Button button_register = (Button) findViewById(R.id.register);
        button_login.setOnClickListener(buttonListener);
        button_register.setOnClickListener(buttonListener);

        Log.i(LoginActivity.class.getName(), "created.");

        // Loads all users
        AssetManager am = this.getAssets();
        try {
            InputStream is = am.open("Users.csv");
            Users.usersFromCSV(CSVReader.parseCsv(is));
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called when the activity is visible to the user.
     */
    @Override
    protected void onStart() {
        super.onStart();

        // clear all values in username and password edit text box when the activity starts
        et_username = findViewById(R.id.username);
        et_password = findViewById(R.id.password);
        et_username.requestFocus();
        et_username.setText("");
        et_password.setText("");

        Log.i(LoginActivity.class.getName(), "started.");
    }

    /**
     * This method resumes the previously running activity when it comes into the foreground.
     */
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

    /**
     * Creates a button click listener for the buttons
     * If the button clicked is the register button, go to the user registration page
     * Else check whether login credentials are valid
     */
    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick (View view) {

            if (view.getId() == R.id.register) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
            else {
                // get the string text from the text editors
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

                // check the validity of the user credentials. Show a warning message if it is invalid.
                Users.User user = Users.userLoginValid(username, Hasher.hash(password));

                if (user == null) {
                    showWarning(resources.getString(R.string.warning_invalid_credential));
                    return;
                }

                // start a new activity, which is searching through the marketplace, when the credentials are valid.
                Intent intent = new Intent(LoginActivity.this, ItemsViewActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
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
     * Check if username and password matches username: comp2100@anu.au with password: comp2100
     * @param username
     * @param password
     * @return true or false
     */
    private boolean checkUserCredential1(String username, String password) {
        username = username.trim();
        password = password.trim();
        return username.equals("comp2100@anu.au") && password.equals("comp2100");
    }

    /**
     * Check if username and password matches username: comp6442@anu.au with password: comp6442
     * @param username
     * @param password
     * @return true or false
     */
    private boolean checkUserCredential2(String username, String password) {
        username = username.trim();
        password = password.trim();
        return username.equals("comp6442@anu.au") && password.equals("comp6442");
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
    private WarningResult checkInputsAndGetWarning(String username,  String password) {

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

