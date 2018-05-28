package com.yan.appwatch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PermissionActivity extends AppCompatActivity {
    private EditText password_edt;
    private Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        password_edt = findViewById(R.id.password_edt);
        submit_btn = findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(btnListener);

    }
    public Button.OnClickListener btnListener = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.submit_btn:
                    if(password_edt.getText().toString().trim().equals(AppConfig.password)){
                        Intent intent = new Intent(PermissionActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(PermissionActivity.this, "密碼錯誤", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        }
    };
}
