package com.aplazame.sdk.demo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aplazame.sdk.AplazameSDK;
import com.aplazame.sdk.network.response.AvailabilityCallback;

public class MainActivity extends AppCompatActivity {

    private static final boolean DEBUG = true;

    private ProgressBar progressBar;
    private EditText amountField;
    private EditText currencyField;
    private EditText accessTokenField;
    private TextView check;
    private EditText checkoutIdField;
    private View layerButton;
    private RelativeLayout goCheckout;

    private boolean isAplazameAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                Double amount = Double.parseDouble(amountField.getText().toString().trim());
                String currency = currencyField.getText().toString().trim();
                String accessToken = accessTokenField.getText().toString().trim();
                if (!accessToken.isEmpty() && amount > 0 && !currency.isEmpty()) {
                    AplazameSDK.setConfiguration(accessToken, DEBUG);

                    AplazameSDK.checkAvailability(amount, currency, new AvailabilityCallback() {
                        @Override
                        public void onAvailable() {
                            // Enable checkout button for instance
                            progressBar.setVisibility(View.GONE);
                            layerButton.setVisibility(View.GONE);
                            isAplazameAvailable = true;
                        }

                        @Override
                        public void onNotAvailable() {
                            // Hide the checkout button for instance
                            progressBar.setVisibility(View.GONE);
                            layerButton.setVisibility(View.VISIBLE);
                            isAplazameAvailable = false;
                            Toast.makeText(MainActivity.this, getString(R.string.aplazame_unavailable_token), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure() {
                            // Hide the checkout button for instance (unknown error)
                            progressBar.setVisibility(View.GONE);
                            layerButton.setVisibility(View.VISIBLE);
                            isAplazameAvailable = false;
                            Toast.makeText(MainActivity.this, getString(R.string.aplazame_unavailable_error), Toast.LENGTH_SHORT).show();
                        }
                    });
                    String checkoutId = checkoutIdField.getText().toString().trim();
                    if (!checkoutId.isEmpty()) {
                        AplazameSDK.setCheckout(checkoutId);
                    }
                }
            }
        });

        goCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAplazameAvailable) {
                    Intent goWebViewAplazame = new Intent(MainActivity.this, WebViewAplazameActivity.class);
                    startActivity(goWebViewAplazame);
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initializeViews() {
        progressBar = findViewById(R.id.progress);
        amountField = findViewById(R.id.amount);
        currencyField = findViewById(R.id.currency);
        accessTokenField = findViewById(R.id.access_token);
        check = findViewById(R.id.check);
        checkoutIdField = findViewById(R.id.checkout_id);
        layerButton = findViewById(R.id.layer_button);
        goCheckout = findViewById(R.id.go_checkout);

        amountField.setText("132.06");
        currencyField.setText("EUR");
    }
}
