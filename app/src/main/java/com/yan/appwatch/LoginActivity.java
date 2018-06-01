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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;
    private String userUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 輸入email, password回傳userID
        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(
                    @NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null) {
                    Log.d("onAuthStateChanged", "登入:"+ user.getUid());
                    userUID =  user.getUid();

                }else{
                    Log.d("onAuthStateChanged", "已登出");
                }
            }
        };
    }


    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authListener);
    }


    // 註冊功能
    private void register(final String email, final String password) {
        final Intent intent = new Intent(this,RegisterActivity.class);
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("登入問題")
                .setMessage("無此帳號，是否要以此帳號與密碼註冊?")
                .setPositiveButton("註冊",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // createUser(email, password);

                                intent.putExtra("email", email);
                                //intent.putExtra("password", password);

                                finish();
                                startActivity(intent);
                            }
                        })
                .setNeutralButton("取消", null)
                .show();
    }

    public void clickRegister(View view) {

        Intent intent = new Intent(this, RegisterActivity.class);
        finish();
        startActivity(intent);

    }

    public void clickLogin(View view) {
        try {
            final String email = ((EditText) findViewById(R.id.email))
                    .getText().toString();
            final String password = ((EditText) findViewById(R.id.password))
                    .getText().toString();
            Log.d("AUTH", email + "/" + password);
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Log.d("onComplete", "登入失敗");
                                register(email, password);
                            }
                        }
                    });
        }
        catch (Exception e)
        {
            Toast.makeText(view.getContext(), "Cannot be empty!",Toast.LENGTH_LONG).show();
        }
    }
}
