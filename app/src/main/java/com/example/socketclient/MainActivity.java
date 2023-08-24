package com.example.socketclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.ByteOrder;

public class MainActivity extends AppCompatActivity {

    private Button pizzaAddButton, burgerAddButton, hotdogAddButton, colaAddButton, orderButton;
    private TextView totalTextView;

    private int[] order = new int[4];
    private int totalPrice = 0, Client_ID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pizzaAddButton = findViewById(R.id.pizzaAddButton);
        burgerAddButton = findViewById(R.id.burgerAddButton);
        hotdogAddButton = findViewById(R.id.hotdogAddButton);
        colaAddButton = findViewById(R.id.colaAddButton);
        orderButton = findViewById(R.id.orderButton);
        totalTextView = findViewById(R.id.totalTextView);

        pizzaAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrderItem(0);
            }
        });

        burgerAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrderItem(1);
            }
        });

        hotdogAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrderItem(2);
            }
        });

        colaAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrderItem(3);
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrderToServer();
            }
        });
    }

    private void addOrderItem(int itemIndex) {
        order[itemIndex]++;
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        totalPrice = order[0] * 10 + order[1] * 8 + order[2] * 6 + order[3] * 5;
        totalTextView.setText("Total: " + totalPrice + " GEL");
    }

    private class SendMessageTask extends AsyncTask<Void, Void, Void> {
        private int[] orderToSend;

        SendMessageTask(int[] orderToSend) {
            this.orderToSend = orderToSend;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("client12345", "doing in background");
            sendOrder(orderToSend);
            Intent intent = new Intent(MainActivity.this, ConfirmationActivity.class);
            intent.putExtra("orderTotal", totalPrice);
            intent.putExtra("clientID", Client_ID);
            startActivity(intent);
            return null;
        }
    }

    private void sendOrder(int[] orderToSend) {
        String serverIP = "192.168.186.92";
        int serverPort = 12345;

        try {
            Socket clientSocket = new Socket(serverIP, serverPort);
            clientSocket.setSoTimeout(5000);

            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());



            // Receive ID
            Log.d("client12345", "starting to recive");
            char receivedChar = (char) dataInputStream.readByte(); // Read a single byte as a character
            Client_ID = Character.getNumericValue(receivedChar); // Convert character to integer
            Log.d("client12345", "recived " + Client_ID);


            for (int i = 0; i < order.length; i++) {
                char charToSend = (char) (order[i] + '0');
                byte byteToSend = (byte) charToSend; // Convert char to byte
                outputStream.writeByte(byteToSend);
                outputStream.flush();
            }


            outputStream.close();
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendOrderToServer() {
        new SendMessageTask(order).execute();
    }
}
