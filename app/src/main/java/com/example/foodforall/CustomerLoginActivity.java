package com.example.foodforall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerLoginActivity extends AppCompatActivity {

    private Button btnRegister,btnLogin;
    private TextView tvStatus,tvRegister;
    private EditText etPassword_cust,etEmail_cust;
    private FirebaseAuth mAuth;
    private DatabaseReference custDatabaseref;
    String onlineCustomerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tvStatus = findViewById(R.id.tvStatus);

        etPassword_cust = findViewById(R.id.etPassword_cust);
        etEmail_cust = findViewById(R.id.etEmail_cust);



        mAuth = FirebaseAuth.getInstance();
        btnRegister.setVisibility(View.INVISIBLE);
        btnRegister.setEnabled(false);
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
                String email = etEmail_cust.getText().toString();
                String password = etPassword_cust.getText().toString();

                RegisterCustomer(email,password);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail_cust.getText().toString();
                String password = etPassword_cust.getText().toString();

                SignInCustomer(email,password);
            }
        });

    }

    private void SignInCustomer(String email, String password) {
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
                                Intent i = new Intent(CustomerLoginActivity.this,CustomerMapActivity.class);
                                startActivity(i);
                                Toast.makeText(CustomerLoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else
                            {
                                Toast.makeText(CustomerLoginActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });
        }
    }

    private void RegisterCustomer(String email, String password) {
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
                                onlineCustomerId = mAuth.getCurrentUser().getUid();
                                custDatabaseref = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(onlineCustomerId);
                                custDatabaseref.setValue(true);

                                Intent i = new Intent(CustomerLoginActivity.this,CustomerMapActivity.class);
                                startActivity(i);
                                Toast.makeText(CustomerLoginActivity.this, "New User Created", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else
                            {
                                Toast.makeText(CustomerLoginActivity.this, "Registration Unsuccesfull", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });
        }
    }
}
