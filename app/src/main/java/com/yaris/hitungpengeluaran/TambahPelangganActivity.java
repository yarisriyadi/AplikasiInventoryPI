package com.yaris.hitungpengeluaran;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TambahPelangganActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText editnama, editalamat, editnohp;
    Button additembuttonpelanggan;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pelanggan);

        editnama = findViewById(R.id.editnama);
        editalamat = findViewById(R.id.editalamat);
        editnohp = findViewById(R.id.editnohp);

        additembuttonpelanggan = findViewById(R.id.additembuttonpelanggan);
        mAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbarpelanggan);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Pelanggan");

        additembuttonpelanggan.setOnClickListener(view -> {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int jumlahPelanggan = (int) dataSnapshot.getChildrenCount();

                    // Membuat ID produk dengan format "PR000X" di mana X adalah jumlahProduk + 1
                    String idPelanggan = "PL" + String.format("%04d", jumlahPelanggan + 1);

                    String item_value_nama = editnama.getText().toString();
                    String item_value_alamat = editalamat.getText().toString();
                    String item_value_nohp = editnohp.getText().toString();

                    if (item_value_nama.isEmpty() || item_value_alamat.isEmpty() || item_value_nohp.isEmpty()) {
                        // Salah satu atau lebih bidang kosong, tampilkan pesan kesalahan
                        Toast.makeText(TambahPelangganActivity.this, "Ups, tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Semua bidang terisi, tambahkan data ke Firebase
                        Pelanggan pelanggan = new Pelanggan(idPelanggan, item_value_nama, item_value_alamat, item_value_nohp);
                        databaseReference.child(idPelanggan).setValue(pelanggan);
                        startActivity(new Intent(TambahPelangganActivity.this, PelangganActivity.class));
                        Toast.makeText(TambahPelangganActivity.this, "Berhasil Ditambah", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                    Toast.makeText(TambahPelangganActivity.this, "Terjadi kesalahan: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TambahPelangganActivity.this, PelangganActivity.class));
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(TambahPelangganActivity.this, PelangganActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
