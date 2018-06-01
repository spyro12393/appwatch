package com.yan.appwatch;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    // private TextView mText;
    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authListener;
    private String userUID;
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();


        // String email = getIntent().getStringExtra("email");
        // String password = getIntent().getStringExtra("password");

        // mText = findViewById(R.id.txtAccount);
        // mText.setText(email);

    }

    public void clickSubmit(View view) {

        String account = ((EditText) findViewById(R.id.txtAccount))
                .getText().toString();
        String password = ((EditText) findViewById(R.id.txtPwd))
                .getText().toString();
        String confirmPassword = ((EditText) findViewById(R.id.txtConfirmPwd))
                .getText().toString();

        Log.d("註冊","email/pwd/confirm: " + account + "/" + password + "/" + confirmPassword);
        if(password.equals(confirmPassword))
        {
            createUser(account, password);
        }
        else
        {
            Toast.makeText(view.getContext(), "確認密碼不同!",Toast.LENGTH_LONG).show();
        }

    }


    private void createUser(String email, String password) {
        Log.d("註冊","email/pwd: " + email + "/" + password);

        final Intent intent = new Intent(this, LoginActivity.class);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                String message =
                                        task.isSuccessful() ? "註冊成功" : "註冊失敗";

                                new AlertDialog.Builder(RegisterActivity.this)
                                        .setMessage(message)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(intent);
                                            }
                                        })
                                        .show();

                            }
                        });
    }


}
