package com.example.bmiapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_PRODUCT = "product_name";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_DELIVERY_ID = "delivery_id";
    private static final String KEY_TERMS = "terms_accepted";

    private EditText editProductName;
    private EditText editQuantity;
    private RadioGroup radioGroupDelivery;
    private CheckBox checkTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editProductName = findViewById(R.id.edit_product_name);
        editQuantity = findViewById(R.id.edit_quantity);
        radioGroupDelivery = findViewById(R.id.radio_group_delivery);
        checkTerms = findViewById(R.id.check_terms);
        Button buttonSave = findViewById(R.id.button_save);
        Button buttonClear = findViewById(R.id.button_clear);

        if (savedInstanceState != null) {
            editProductName.setText(savedInstanceState.getString(KEY_PRODUCT, ""));
            editQuantity.setText(savedInstanceState.getString(KEY_QUANTITY, ""));
            int savedDeliveryId = savedInstanceState.getInt(KEY_DELIVERY_ID, -1);
            if (savedDeliveryId != -1) {
                radioGroupDelivery.check(savedDeliveryId);
            }
            checkTerms.setChecked(savedInstanceState.getBoolean(KEY_TERMS, false));
        }

        buttonSave.setOnClickListener(v -> {
            if (validateForm()) {
                saveForm();
            }
        });

        buttonClear.setOnClickListener(v -> clearForm());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_PRODUCT, editProductName.getText().toString());
        outState.putString(KEY_QUANTITY, editQuantity.getText().toString());
        outState.putInt(KEY_DELIVERY_ID, radioGroupDelivery.getCheckedRadioButtonId());
        outState.putBoolean(KEY_TERMS, checkTerms.isChecked());
    }

    private boolean validateForm() {
        boolean valid = true;

        String productName = editProductName.getText().toString().trim();
        if (productName.isEmpty()) {
            editProductName.setError(getString(R.string.error_required));
            valid = false;
        } else {
            editProductName.setError(null);
        }

        String quantityStr = editQuantity.getText().toString().trim();
        if (quantityStr.isEmpty()) {
            editQuantity.setError(getString(R.string.error_required));
            valid = false;
        } else {
            try {
                int quantity = Integer.parseInt(quantityStr);
                if (quantity < 1 || quantity > 99) {
                    editQuantity.setError(getString(R.string.error_quantity_range));
                    valid = false;
                } else {
                    editQuantity.setError(null);
                }
            } catch (NumberFormatException e) {
                editQuantity.setError(getString(R.string.error_quantity_range));
                valid = false;
            }
        }

        if (radioGroupDelivery.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, R.string.error_delivery_required, Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (!checkTerms.isChecked()) {
            checkTerms.setError(getString(R.string.error_terms_required));
            Toast.makeText(this, R.string.error_terms_required, Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            checkTerms.setError(null);
        }

        return valid;
    }

    private void saveForm() {
        String productName = editProductName.getText().toString().trim();
        int quantity = Integer.parseInt(editQuantity.getText().toString().trim());

        int selectedId = radioGroupDelivery.getCheckedRadioButtonId();
        RadioButton selectedRadio = findViewById(selectedId);
        String deliveryMethod = selectedRadio.getText().toString();

        String summary = getString(R.string.toast_summary, productName, quantity, deliveryMethod);
        Toast.makeText(this, summary, Toast.LENGTH_LONG).show();
    }

    private void clearForm() {
        editProductName.setText("");
        editQuantity.setText("");
        editProductName.setError(null);
        editQuantity.setError(null);
        radioGroupDelivery.clearCheck();
        checkTerms.setChecked(false);
        checkTerms.setError(null);
        editProductName.requestFocus();
    }
}