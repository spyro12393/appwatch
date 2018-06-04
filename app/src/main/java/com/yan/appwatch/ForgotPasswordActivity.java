package com.yan.appwatch;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
    }

    public  void ForgetPassword(String emailAddress) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Reset password", "Email sent.");
                        }
                    }
                });

    }

    // 完成忘記密碼驗證，還沒有完成與身分證一起的驗證。
    public void clickFinish(View view) {
        String email = ((EditText) findViewById(R.id.txtEmail)).getText().toString();
        String pid = ((EditText) findViewById(R.id.txtPid)).getText().toString();

        ForgetPassword(email);
        Toast.makeText(view.getContext(), "已寄信至您的信箱，請至信箱查看!",Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, LoginActivity.class);
        finish();
        startActivity(intent);


    }
}
