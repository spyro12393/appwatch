package com.yan.appwatch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

public class SetUseTimeActivity extends AppCompatActivity {

    private NumberPicker HourPicker,MinPicker;
    private Button setUseTime_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_use_time);
        HourPicker = findViewById(R.id.hour_numberPicker);
        MinPicker = findViewById(R.id.min_numberPicker);
        setUseTime_btn = findViewById(R.id.set_time_btn);
        HourPicker.setMaxValue(24);
        HourPicker.setMinValue(0);
        HourPicker.setValue(0);
        MinPicker.setMaxValue(59);
        MinPicker.setMinValue(0);
        MinPicker.setValue(0);
    }


    public void setUseTime(View view) {
        AppConfig.allowuseTime = HourPicker.getValue()*3600000+MinPicker.getValue()*60000;
        startService(new Intent(SetUseTimeActivity.this, TimeService.class));
        String temp = "今日可使用："+String.valueOf(HourPicker.getValue())+"："+String.valueOf(MinPicker.getValue());
        Toast.makeText(this,temp,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SetUseTimeActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
