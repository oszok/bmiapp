package com.example.bmiapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LifecycleTag";
    private static final String KEY_RESULT = "result";

    private EditText editWeight;
    private EditText editHeight;
    private CheckBox checkMetric;
    private TextView textResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        editWeight = findViewById(R.id.edit_weight);
        editHeight = findViewById(R.id.edit_height);
        checkMetric = findViewById(R.id.check_metric);
        textResult = findViewById(R.id.text_result);
        Button buttonCalc = findViewById(R.id.button_calc);
        Button buttonClear = findViewById(R.id.button_clear);

        buttonCalc.setOnClickListener(v -> {
            if (validate()) {
                calculate();
            }
        });
        buttonClear.setOnClickListener(v -> clear());

        if (savedInstanceState != null) {
            String result = savedInstanceState.getString(KEY_RESULT);
            if (result != null) {
                textResult.setText(result);
                textResult.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (textResult.getVisibility() == View.VISIBLE) {
            outState.putString(KEY_RESULT, textResult.getText().toString());
        }
    }

    private boolean validate() {
        boolean valid = true;

        String weightText = editWeight.getText().toString().trim();
        if (weightText.isEmpty()) {
            editWeight.setError(getString(R.string.error_required));
            valid = false;
        } else {
            double weight = Double.parseDouble(weightText);
            if (weight < 20 || weight > 300) {
                editWeight.setError(getString(R.string.error_weight_range));
                valid = false;
            }
        }

        String heightText = editHeight.getText().toString().trim();
        if (heightText.isEmpty()) {
            editHeight.setError(getString(R.string.error_required));
            valid = false;
        } else {
            double height = Double.parseDouble(heightText);
            if (height < 50 || height > 250) {
                editHeight.setError(getString(R.string.error_height_range));
                valid = false;
            }
        }

        return valid;
    }

    private void calculate() {
        double weight = Double.parseDouble(editWeight.getText().toString().trim());
        double heightCm = Double.parseDouble(editHeight.getText().toString().trim());

        if (!checkMetric.isChecked()) {
            weight = weight * 0.4536;     // funty -> kg
            heightCm = heightCm * 2.54;   // cale -> cm
        }

        double heightM = heightCm / 100;
        double bmi = weight / (heightM * heightM);

        textResult.setText(getString(R.string.result_format, bmi, categoryFor(bmi)));
        textResult.setVisibility(View.VISIBLE);
    }

    private String categoryFor(double bmi) {
        if (bmi < 18.5) return getString(R.string.bmi_under);
        if (bmi < 25) return getString(R.string.bmi_normal);
        if (bmi < 30) return getString(R.string.bmi_over);
        return getString(R.string.bmi_obese);
    }

    private void clear() {
        editWeight.setText("");
        editHeight.setText("");
        editWeight.setError(null);
        editHeight.setError(null);
        textResult.setVisibility(View.GONE);
        editWeight.requestFocus();
    }
}