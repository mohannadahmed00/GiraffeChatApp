package com.example.mychat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

public class SignUpActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public static int RESULT_LOAD_IMAGE = 1;
    static FirebaseAuth auth;
    Button btnGoogle;
    Button btnSignUp;
    DatabaseReference databaseReference;
    EditText etEmail;
    EditText etName;
    EditText etPassword;
    EditText etPhone;
    EditText etRePassword;
    ImageView ivUserImg;
    StorageReference storageReference;
    TextView tvLogin;
    TextView tvShowPassword;
    TextView tvShowRePassword;
    StorageTask uploadTask;
    Uri userImgGlobalUri;
    Uri userImgLocalUri;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();
        this.databaseReference = FirebaseDatabase.getInstance().getReference().child("user info");
        this.storageReference = FirebaseStorage.getInstance().getReference().child("user image");
        this.ivUserImg = (ImageView) findViewById(R.id.iv_user_img_activity_sign_up);
        this.etName = (EditText) findViewById(R.id.et_name_activity_sign_up);
        this.etPhone = (EditText) findViewById(R.id.et_phone_activity_sign_up);
        this.etEmail = (EditText) findViewById(R.id.et_email_activity_sign_up);
        this.etPassword = (EditText) findViewById(R.id.et_password_activity_sign_up);
        this.etRePassword = (EditText) findViewById(R.id.et_re_password_activity_sign_up);
        this.tvShowPassword = (TextView) findViewById(R.id.tv_show_password_activity_sign_up);
        this.tvShowRePassword = (TextView) findViewById(R.id.tv_show_re_password_activity_sign_up);
        this.tvLogin = (TextView) findViewById(R.id.tv_login_activity_sign_up);
        this.btnSignUp = (Button) findViewById(R.id.btn_sign_up_activity_sign_up);
        this.btnGoogle = (Button) findViewById(R.id.btn_google_activity_sign_up);
        this.btnSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                com.example.mychat.SignUpActivity.this.createAccount();
            }
        });
        this.btnGoogle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(com.example.mychat.SignUpActivity.this, "Google+", Toast.LENGTH_SHORT).show();
            }
        });
        this.tvLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(com.example.mychat.SignUpActivity.this, com.example.mychat.LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                com.example.mychat.SignUpActivity.this.startActivity(intent);
            }
        });
        this.ivUserImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                com.example.mychat.SignUpActivity.this.startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), com.example.mychat.SignUpActivity.RESULT_LOAD_IMAGE);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void createAccount() {
        final String name = this.etName.getText().toString().trim();
        final String email = this.etEmail.getText().toString().trim();
        final String phone = this.etPhone.getText().toString().trim();
        String password = this.etPassword.getText().toString().trim();
        String rePassword = this.etRePassword.getText().toString().trim();
        if (!name.equals("") && !phone.equals("") && !email.equals("") && !password.equals("") && password.length() >= 8 && password.equals(rePassword) && this.userImgLocalUri != null) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        StorageReference storageReference = com.example.mychat.SignUpActivity.this.storageReference;
                        StringBuilder sb = new StringBuilder();
                        sb.append(System.currentTimeMillis());
                        sb.append(".");
                        com.example.mychat.SignUpActivity signUpActivity = com.example.mychat.SignUpActivity.this;
                        sb.append(signUpActivity.getFileExe(signUpActivity.userImgLocalUri));
                        final StorageReference imgReference = storageReference.child(sb.toString());
                        com.example.mychat.SignUpActivity signUpActivity2 = com.example.mychat.SignUpActivity.this;
                        signUpActivity2.uploadTask = imgReference.putFile(signUpActivity2.userImgLocalUri);
                        com.example.mychat.SignUpActivity.this.uploadTask.continueWithTask(new Continuation() {
                            public Object then(Task task) throws Exception {
                                if (task.isComplete()) {
                                    return imgReference.getDownloadUrl();
                                }
                                throw task.getException();
                            }
                        }).addOnCompleteListener(new OnCompleteListener() {
                            public void onComplete(Task task) {
                                if (task.isSuccessful()) {
                                    com.example.mychat.SignUpActivity.this.userImgGlobalUri = (Uri) task.getResult();
                                    String x = com.example.mychat.SignUpActivity.this.userImgGlobalUri.toString();
                                    com.example.mychat.SignUpActivity.this.databaseReference = com.example.mychat.SignUpActivity.this.databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    com.example.mychat.SignUpActivity.this.databaseReference.setValue(new com.example.mychat.User(x, name, email, phone));
                                    com.example.mychat.SignUpActivity.auth.signOut();
                                    Intent intent = new Intent(com.example.mychat.SignUpActivity.this, com.example.mychat.LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    com.example.mychat.SignUpActivity.this.startActivity(intent);
                                    return;
                                }
                                Toast.makeText(com.example.mychat.SignUpActivity.this, "error: data insertion ", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                    Toast.makeText(com.example.mychat.SignUpActivity.this, "Account creation failed", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (email.equals("") || password.equals("")) {
            Toast.makeText(this, "Please enter your information", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 8) {
            Toast.makeText(this, "Password is too short", Toast.LENGTH_SHORT).show();
        } else if (!this.etPassword.getText().toString().trim().equals(this.etRePassword.getText().toString().trim())) {
            Toast.makeText(this, "Password is not match", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show();
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == -1 && data != null) {
            Uri data2 = data.getData();
            this.userImgLocalUri = data2;
            this.ivUserImg.setImageURI(data2);
        }
    }

    /* access modifiers changed from: package-private */
    public String getFileExe(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(uri));
    }
}
