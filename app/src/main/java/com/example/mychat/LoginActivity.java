package com.example.mychat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    static FirebaseAuth auth;
    static FirebaseAuth.AuthStateListener authStateListener;
    Button btnLogin;
    Button btnSignUp;
    EditText etPassword;
    EditText etUsername;
    TextView tvShowPassword;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                if (com.example.mychat.LoginActivity.auth.getCurrentUser() != null) {
                    Intent intent = new Intent(com.example.mychat.LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    com.example.mychat.LoginActivity.this.startActivity(intent);
                }
            }
        };
        auth.addAuthStateListener(authStateListener);
        this.etUsername = (EditText) findViewById(R.id.et_username_activity_login);
        this.etPassword = (EditText) findViewById(R.id.et_password_activity_login);
        this.btnLogin = (Button) findViewById(R.id.btn_login_activity_login);
        this.btnSignUp = (Button) findViewById(R.id.btn_sign_up_activity_login);
        this.tvShowPassword = (TextView) findViewById(R.id.tv_show_password_activity_login);
        this.btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                com.example.mychat.LoginActivity.this.login();
            }
        });
        this.btnSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(com.example.mychat.LoginActivity.this, "wait!! not now", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void login() {
        String username = this.etUsername.getText().toString().trim();
        String password = this.etPassword.getText().toString().trim();
        if (username.equals("") || password.equals("")) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
        } else {
            auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(com.example.mychat.LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        com.example.mychat.LoginActivity.this.startActivity(intent);
                        return;
                    }
                    Toast.makeText(com.example.mychat.LoginActivity.this, "Username or Password is incorrect", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
