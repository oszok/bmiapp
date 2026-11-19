package com.example.bmiapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editName;
    private CheckBox checkAgree;
    private Switch switchActive;
    private RadioGroup radioGroupSize;
    private SeekBar seekVolume;
    private Button buttonCollect;
    private TextView textSummary;
    private TextView textSeekLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.edit_name);
        checkAgree = findViewById(R.id.check_agree);
        switchActive = findViewById(R.id.switch_active);
        radioGroupSize = findViewById(R.id.radio_group_size);
        seekVolume = findViewById(R.id.seek_volume);
        buttonCollect = findViewById(R.id.button_collect);
        textSummary = findViewById(R.id.text_summary);
        textSeekLabel = findViewById(R.id.text_seek_label);

        seekVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textSeekLabel.setText("Głośność / wartość: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        buttonCollect.setOnClickListener(v -> collectAndDisplayState());
    }

    private void collectAndDisplayState() {
        String name = editName.getText().toString().trim();
        if (name.isEmpty()) {
            name = "Brak";
        }

        String agree = checkAgree.isChecked() ? getString(R.string.yes) : getString(R.string.no);
        String active = switchActive.isChecked() ? getString(R.string.yes) : getString(R.string.no);

        String selectedSize = "-";
        int checkedRadioId = radioGroupSize.getCheckedRadioButtonId();
        if (checkedRadioId != -1) {
            RadioButton selectedButton = findViewById(checkedRadioId);
            selectedSize = selectedButton.getText().toString();
        }

        int volume = seekVolume.getProgress();

        String summary = getString(R.string.summary_format, name, agree, active, selectedSize, volume);
        textSummary.setText(summary);
    }
}