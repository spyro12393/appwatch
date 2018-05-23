package com.yan.appwatch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class TipActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);
    }

    public void close (View view){
        Intent intent = new Intent(TipActivity.this,MainActivity.class);
        startActivity(intent);
        finish();

    }
}