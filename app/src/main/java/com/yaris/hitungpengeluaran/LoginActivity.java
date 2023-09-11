package com.yaris.hitungpengeluaran;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText editTextEmail, editTextPassword;
        ProgressDialog progressDialog;
        Button login;
        FirebaseAuth mAuth;

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        editTextEmail = findViewById(R.id.emailSignIn);
        editTextPassword = findViewById(R.id.password);
        login = findViewById(R.id.Login);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mAuth.getCurrentUser();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_to_next_activity();
//                return false;
            }

            private void login_to_next_activity() {
                String email = Objects.requireNonNull(editTextEmail.getText()).toString();
                String password = Objects.requireNonNull(editTextPassword.getText()).toString().trim();

                if (!email.matches(emailPattern)) {
                    editTextEmail.setError("Periksa Kembali");
                    editTextEmail.requestFocus();
                } else if (password.isEmpty() || password.length() < 6) {
                    editTextPassword.setError("Periksa Kembali");
                    editTextPassword.requestFocus();
                } else {

                    if (isNetworkAvailable()) {
                        progressDialog.setMessage("Mohon Tunggu!");
                        progressDialog.setTitle("Loading");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                String gm = editTextEmail.getText().toString();
                                intent.putExtra("gmail", gm);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                                Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(LoginActivity.this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
                    }
                }
            }


            // Method untuk memeriksa ketersediaan koneksi internet
            private boolean isNetworkAvailable() {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager != null) {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
                }
                return false;
            }
        });
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
