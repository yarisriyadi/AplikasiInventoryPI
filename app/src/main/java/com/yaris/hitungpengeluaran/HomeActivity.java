package com.yaris.hitungpengeluaran;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private CardView stokbarang, pengeluaran, transaksi, pelanggan,about, exit;
    FirebaseAuth mAuth;
    TextView firebasenameview;
    private FirebaseAuth firebaseAuth;

    FirebaseUser muser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        stokbarang = findViewById(R.id.stokbaranghome);
        pengeluaran = findViewById(R.id.pengeluaranhome);
        pelanggan = findViewById(R.id.pelanggan);
        transaksi = findViewById(R.id.laporantransaksihome);
        about = findViewById(R.id.abouthome);
        exit = findViewById(R.id.exithome);

        firebasenameview = findViewById(R.id.firebasename);
        mAuth =FirebaseAuth.getInstance();
        muser = mAuth.getCurrentUser();

        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser=users.getEmail();
        String result = finaluser.substring(0, finaluser.indexOf("@"));
        String resultemail = result.replace(".","");
        firebasenameview.setText("Welcome, "+resultemail);

        //StokBarang
        stokbarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProdukActivity.class);
                startActivity(intent);
            }
        });

        //Pengeluaran
        pengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PengeluaranActivity.class);
                startActivity(intent);
            }
        });

        //Transaksi
        transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TransaksiActivity.class);
                startActivity(intent);
            }
        });

        //Pemasukan
        pelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PelangganActivity.class);
                startActivity(intent);
            }
        });

        //About
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        //Exit
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLogout();
            }
        });
    }

    private void confirmLogout() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
        alertDialogBuilder.setMessage("Apakah Kamu Ingin Keluar?");

        alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(HomeActivity.this, "Berhasil Keluar", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        });

        alertDialogBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        finish();
    }
}
