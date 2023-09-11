package com.yaris.hitungpengeluaran;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TambahProdukActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText item_name, item_cat, item_price, item_exp, item_stok;
    Button add_item_button;
    Button scanbutton;
    TextView itembarcode, TextstatusPr;
    Spinner item_status_spinner;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    public static TextView resulttextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_produk);

        item_name = findViewById(R.id.edititemname);
        item_cat = findViewById(R.id.editcategory);
        item_price = findViewById(R.id.editprice);
        item_stok = findViewById(R.id.editstok);
        item_exp = findViewById(R.id.editexp);
        TextstatusPr = findViewById(R.id.TextStatus);
        scanbutton = findViewById(R.id.buttonscan);
        itembarcode = findViewById(R.id.barcodeview);
        resulttextview = findViewById(R.id.barcodeview);
        add_item_button = findViewById(R.id.additembuttontodatabase);
        mAuth = FirebaseAuth.getInstance();

        item_status_spinner = findViewById(R.id.spinnerStatus);

        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this, R.array.item_status_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        item_status_spinner.setAdapter(statusAdapter);

        toolbar = findViewById(R.id.toolbartambahstok);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        item_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        item_status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedStatus = item_status_spinner.getSelectedItem().toString();

                TextstatusPr.setText("Status: " + selectedStatus);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Produk");
        scanbutton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ScanCodeActivity.class)));

        add_item_button.setOnClickListener(view -> {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int jumlahProduk = (int) dataSnapshot.getChildrenCount();

                    String idProduk = "PR" + String.format("%04d", jumlahProduk + 1);

                    String item_value_name = item_name.getText().toString();
                    String item_value_Cat = item_cat.getText().toString();
                    String item_value_price = item_price.getText().toString();
                    String item_value_stok = item_stok.getText().toString();
                    String item_value_exp = item_exp.getText().toString();
                    String itembarcodeValue = itembarcode.getText().toString();

                    String item_value_status = item_status_spinner.getSelectedItem().toString();

                    if (item_value_name.isEmpty() || item_value_Cat.isEmpty() || item_value_price.isEmpty() || item_value_stok.isEmpty()) {
                        Toast.makeText(TambahProdukActivity.this, "Ups, tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    } else {
                        Produk produk = new Produk(idProduk, item_value_name, item_value_Cat, item_value_price, item_value_stok, item_value_exp, itembarcodeValue, item_value_status);
                        databaseReference.child(idProduk).setValue(produk);
                        startActivity(new Intent(TambahProdukActivity.this, ProdukActivity.class));
                        Toast.makeText(TambahProdukActivity.this, "Berhasil Ditambah", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                    Toast.makeText(TambahProdukActivity.this, "Terjadi kesalahan: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
                TambahProdukActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        calendar.set(year, month, dayOfMonth);
                        String selectedDate = dateFormat.format(calendar.getTime());
                        item_exp.setText(selectedDate);
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TambahProdukActivity.this, ProdukActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(TambahProdukActivity.this, ProdukActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
