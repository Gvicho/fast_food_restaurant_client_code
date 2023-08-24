package com.example.socketclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ConfirmationActivity extends AppCompatActivity {

    private TextView confirmationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        confirmationTextView = findViewById(R.id.confirmationTextView);

        int orderTotal = getIntent().getIntExtra("orderTotal", 0);
        int Order_ID = getIntent().getIntExtra("clientID", -1);

        confirmationTextView.setText("Your id is :   " + Order_ID + "\nPrize - " + orderTotal + " GEL\nThanks for your order!");
    }
}