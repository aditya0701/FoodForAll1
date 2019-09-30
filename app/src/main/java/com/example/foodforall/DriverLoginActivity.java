package com.example.foodforall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverLoginActivity extends AppCompatActivity {

    private Button btnRegister,btnLogin;
    private TextView tvStatus,tvRegister;
    private EditText etPassword_ngo,etEmail_ngo;
    private FirebaseAuth mAuth;
    private DatabaseReference ngoDatabaseref;
    String onlineNgoId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tvStatus = findViewById(R.id.tvStatus);
        etPassword_ngo = findViewById(R.id.etPassword_ngo);
        etEmail_ngo = findViewById(R.id.etEmail_ngo);



        mAuth = FirebaseAuth.getInstance();
        btnRegister.setVisibility(View.INVISIBLE);
        btnRegister.setEnabled(false);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLogin.setVisibility(View.INVISIBLE);
                tvRegister.setVisibility(View.INVISIBLE);
                tvStatus.setText("Register");
                btnRegister.setVisibility(View.VISIBLE);
                btnRegister.setEnabled(true);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail_ngo.getText().toString();
                String password = etPassword_ngo.getText().toString();

                RegisterDriver(email,password);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail_ngo.getText().toString();
                String password = etPassword_ngo.getText().toString();

                SignInDriver(email,password);
            }
        });
    }

    private void SignInDriver(String email, String password) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.layout_loading_dialog);
        final AlertDialog dialog = builder.create();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter the email", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter the password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            dialog.show();
            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Intent i =  new Intent(DriverLoginActivity.this,NgoMapActivity.class);
                                startActivity(i);
                                Toast.makeText(DriverLoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else
                            {
                                Toast.makeText(DriverLoginActivity.this, "Login Unsuccessfully", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });
        }
    }

    private void RegisterDriver(String email, String password) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.layout_loading_dialog);
        final AlertDialog dialog = builder.create();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter the email", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter the password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            dialog.show();
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                onlineNgoId = mAuth.getCurrentUser().getUid();
                                ngoDatabaseref = FirebaseDatabase.getInstance().getReference().child("Users").child("Ngo").child(onlineNgoId);
                                ngoDatabaseref.setValue(true);

                                Intent i = new Intent(DriverLoginActivity.this,NgoMapActivity.class);
                                startActivity(i);
                                Toast.makeText(DriverLoginActivity.this, "New User Created", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else
                            {
                                Toast.makeText(DriverLoginActivity.this, "Registration Unsuccesfull", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });
        }
    }
}
