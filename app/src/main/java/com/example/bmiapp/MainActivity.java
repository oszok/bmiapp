package com.example.bmiapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_RESULT = "result";

    private EditText editWeight;
    private EditText editHeight;
    private RadioGroup radioGroupUnits;
    private TextView textResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editWeight = findViewById(R.id.edit_weight);
        editHeight = findViewById(R.id.edit_height);
        radioGroupUnits = findViewById(R.id.radio_group_units);
        textResult = findViewById(R.id.text_result);
        Button buttonCalc = findViewById(R.id.button_calc);
        Button buttonClear = findViewById(R.id.button_clear);

        radioGroupUnits.setOnCheckedChangeListener((group, checkedId) -> {
            updateHints(checkedId == R.id.radio_metric);
            editWeight.setError(null);
            editHeight.setError(null);
        });

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

        updateHints(isMetricSelected());
    }

    private boolean isMetricSelected() {
        return radioGroupUnits.getCheckedRadioButtonId() == R.id.radio_metric;
    }

    private void updateHints(boolean isMetric) {
        if (isMetric) {
            editWeight.setHint(R.string.hint_weight);
            editHeight.setHint(R.string.hint_height);
        } else {
            editWeight.setHint(R.string.hint_weight_lb);
            editHeight.setHint(R.string.hint_height_in);
        }
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
        boolean metric = isMetricSelected();

        String weightText = editWeight.getText().toString().trim();
        if (weightText.isEmpty()) {
            editWeight.setError(getString(R.string.error_required));
            valid = false;
        } else {
            double weight = Double.parseDouble(weightText);
            double weightKg = metric ? weight : weight * 0.4536;
            if (weightKg < 20 || weightKg > 300) {
                editWeight.setError(getString(metric ? R.string.error_weight_range : R.string.error_weight_range_lb));
                valid = false;
            }
        }

        String heightText = editHeight.getText().toString().trim();
        if (heightText.isEmpty()) {
            editHeight.setError(getString(R.string.error_required));
            valid = false;
        } else {
            double height = Double.parseDouble(heightText);
            double heightCm = metric ? height : height * 2.54;
            if (heightCm < 50 || heightCm > 250) {
                editHeight.setError(getString(metric ? R.string.error_height_range : R.string.error_height_range_in));
                valid = false;
            }
        }

        return valid;
    }

    private void calculate() {
        double weight = Double.parseDouble(editWeight.getText().toString().trim());
        double heightCm = Double.parseDouble(editHeight.getText().toString().trim());

        if (!isMetricSelected()) {
            weight = weight * 0.4536;     // lb -> kg
            heightCm = heightCm * 2.54;   // in -> cm
        }

        double heightM = heightCm / 100.0;
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