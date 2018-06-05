package com.yan.appwatch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

public class SetUseTimeActivity extends AppCompatActivity {

    private NumberPicker HourPicker;
    private Button setUseTime_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_use_time);
        HourPicker = findViewById(R.id.hour_numberPicker);
        setUseTime_btn = findViewById(R.id.set_time_btn);
        HourPicker.setMaxValue(24);
        HourPicker.setMinValue(0);
        HourPicker.setValue(0);
    }


    public void setUseTime(View view) {
        AppConfig.allowuseTime = HourPicker.getValue()*3600000;
        startService(new Intent(SetUseTimeActivity.this, TimeService.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SetUseTimeActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
