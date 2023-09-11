package com.yaris.hitungpengeluaran;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TambahPengeluaranActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText item_keterangan, item_tanggal, item_jumlahuang,item_jumlah, item_sumber;
    Button add_item_button;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pengeluaran);

        item_keterangan = findViewById(R.id.edititemketerangan);
        item_tanggal = findViewById(R.id.edittanggal);
        item_jumlahuang = findViewById(R.id.editjumlahuang);
        item_jumlah = findViewById(R.id.editjumlahProduk);
        item_sumber = findViewById(R.id.editsumber);

        add_item_button = findViewById(R.id.additembuttonpengeluaran);
        mAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbarpengeluaran);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Pengeluaran");

        item_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        add_item_button.setOnClickListener(view -> {

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int jumlahPengeluaran = (int) dataSnapshot.getChildrenCount();

                    String idPengeluaran = "PN" + String.format("%04d", jumlahPengeluaran + 1);

                    String item_value_keterangan = item_keterangan.getText().toString();
                    String item_value_tanggal = item_tanggal.getText().toString();
                    String item_value_jumlahuang = item_jumlahuang.getText().toString();
                    String item_value_jumlahProduk = item_jumlah.getText().toString();
                    String item_value_sumber = item_sumber.getText().toString();


                    if (item_value_keterangan.isEmpty() || item_value_tanggal.isEmpty() || item_value_jumlahuang.isEmpty()) {
                        Toast.makeText(TambahPengeluaranActivity.this, "Ups, tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Semua bidang terisi, tambahkan data ke Firebase
                        Pengeluaran pengeluaran = new Pengeluaran(idPengeluaran, item_value_jumlahProduk,item_value_keterangan, item_value_tanggal, item_value_jumlahuang, item_value_sumber);
                        databaseReference.child(idPengeluaran).setValue(pengeluaran);
                        startActivity(new Intent(TambahPengeluaranActivity.this, PengeluaranActivity.class));
                        Toast.makeText(TambahPengeluaranActivity.this, "Berhasil Ditambah", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(TambahPengeluaranActivity.this, "Terjadi kesalahan: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                TambahPengeluaranActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        calendar.set(year, month, dayOfMonth);
                        String selectedDate = dateFormat.format(calendar.getTime());
                        item_tanggal.setText(selectedDate);
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TambahPengeluaranActivity.this, PengeluaranActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(TambahPengeluaranActivity.this, PengeluaranActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
