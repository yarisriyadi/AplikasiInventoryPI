package com.yaris.hitungpengeluaran;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TambahTransaksiActivity extends AppCompatActivity {

    EditText editTextKuantitas, editTextUangBayar, editTexthargapr, Editkembalian, EdittotalHarga;
    TextView textViewTanggalPembelian;
    ArrayList<String> produkList = new ArrayList<>();
    ArrayList<String> pelangganList = new ArrayList<>();

    ArrayAdapter<String> produkAdapter, pelangganAdapter;
    Spinner spinnerProduk, spinnerPelanggan;
    Button buttonAdd;
    TextView TextProduktr, TextPelanggantr;
    Toolbar toolbar;
    String tanggalPembelian = "";
    String selectedProduk = "";
    String selectedPelanggan = "";

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_transaksi);

        spinnerProduk = findViewById(R.id.spinnerProduk);
        spinnerPelanggan = findViewById(R.id.spinnerPelanggan);

        TextProduktr = findViewById(R.id.TextProduktr);
        TextPelanggantr = findViewById(R.id.TextPelanggantr);

        editTextKuantitas = findViewById(R.id.Editkuantitas);

        Editkembalian = findViewById(R.id.Editkembalian);
        Editkembalian.setEnabled(false);
        EdittotalHarga = findViewById(R.id.EdittotalHarga);
        EdittotalHarga.setEnabled(false);

        editTexthargapr = findViewById(R.id.edithargapr);
        editTexthargapr.setEnabled(false);
        editTextUangBayar = findViewById(R.id.EdituangBayar);

        textViewTanggalPembelian = findViewById(R.id.tanggalPembelian);

        mAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbarcatattransaksi);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        buttonAdd = findViewById(R.id.btnCatatTransaksi);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTransaksi();
            }
        });
        spinnerProduk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    selectedProduk = produkAdapter.getItem(position);
                    TextProduktr.setText(selectedProduk);
                    calculateTotalHarga();

                    String[] splittedProdukHarga = selectedProduk.split(" \\| ");
                    if (splittedProdukHarga.length == 2) {
                        String hargaProdukString = splittedProdukHarga[1].replace(",", "").trim();
                        editTexthargapr.setText(hargaProdukString);
                    }
                } else {
                    TextProduktr.setText("");
                    editTexthargapr.setText("");
                    EdittotalHarga.setText("");
                    Editkembalian.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinnerPelanggan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    selectedPelanggan = pelangganAdapter.getItem(position);

                    TextPelanggantr.setText(selectedPelanggan);
                } else {
                    selectedPelanggan = "";
                    TextPelanggantr.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        editTextKuantitas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                calculateTotalHarga();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editTextUangBayar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                calculateUangKembalian();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        populateProdukSpinner();
        populatePelangganSpinner();

    }
    private void calculateTotalHarga() {
        String produkHargaString = spinnerProduk.getSelectedItem().toString();
        String[] splittedProdukHarga = produkHargaString.split(" \\| ");

        if (splittedProdukHarga.length == 2) {
            String hargaProdukString = splittedProdukHarga[1].replace(",", "").trim();
            int hargaProduk = Integer.parseInt(hargaProdukString);

            String kuantitasInput = editTextKuantitas.getText().toString().trim();

            if (!kuantitasInput.isEmpty()) {
                if (!kuantitasInput.startsWith("-") && !kuantitasInput.equals("0")) {
                    int kuantitas = Integer.parseInt(kuantitasInput);
                    int totalHarga = hargaProduk * kuantitas;
                    EdittotalHarga.setText("" + totalHarga);
                    calculateUangKembalian();

                } else {
                    Toast.makeText(this, "Kuantitas harus lebih dari 0", Toast.LENGTH_SHORT).show();
                    EdittotalHarga.setText("");
                }
            } else {
                EdittotalHarga.setText("");
            }
        }
    }

    private void calculateUangKembalian() {
        try {
            String uangBayarInput = editTextUangBayar.getText().toString().trim();
            String totalHargaString = EdittotalHarga.getText().toString().replace("Rp. ", "").replace(",", "").trim();

            if (!uangBayarInput.isEmpty() && !totalHargaString.isEmpty()) {
                int uangBayar = Integer.parseInt(uangBayarInput);
                int totalHarga = Integer.parseInt(totalHargaString);

                int uangKembalian = uangBayar - totalHarga;

                // Tampilkan uang kembalian
                Editkembalian.setText("" + uangKembalian);
            } else {
                Editkembalian.setText("0");
            }
        } catch (NumberFormatException e) {
            // Tangani kesalahan saat parsing menjadi integer
            e.printStackTrace();
        }
    }

    private void addTransaksi() {
        databaseReference.child("Transaksi").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int jumlahTransaksi = (int) dataSnapshot.getChildrenCount();

                String idTransaksi = "TR" + String.format("%04d", jumlahTransaksi + 1);

                String nameproduk = TextProduktr.getText().toString();
                String namePembeli = TextPelanggantr.getText().toString();
                String pricetr = editTexthargapr.getText().toString();
                String kuantitastr = editTextKuantitas.getText().toString();
                String totalhargatr = EdittotalHarga.getText().toString();
                String uangBayartr = editTextUangBayar.getText().toString();
                String uangKembaliantr = Editkembalian.getText().toString();

                if (selectedProduk.equals("Pilih Produk") || kuantitastr.isEmpty() || uangBayartr.isEmpty() || tanggalPembelian.isEmpty()) {
                    Toast.makeText(TambahTransaksiActivity.this, "Harap isi semua dengan benar", Toast.LENGTH_SHORT).show();
                    return;
                }

                String produkHargaString = spinnerProduk.getSelectedItem().toString();
                String[] splittedProdukHarga = produkHargaString.split(" \\| ");
                String namaProduk = splittedProdukHarga[0];
                String hargaProdukString = splittedProdukHarga[1].replace(",", ""); // Hapus tanda koma jika ada

                int hargaProduk = Integer.parseInt(hargaProdukString);
                int kuantitas = Integer.parseInt(editTextKuantitas.getText().toString());

                String uangBayarInput = editTextUangBayar.getText().toString().trim();
                int uangBayar = 0;
                int uangKembalian = 0; // Inisialisasi uang kembalian dengan nilai awal 0
                int totalHarga = 0;
                if (!uangBayarInput.isEmpty()) {
                    uangBayar = Integer.parseInt(uangBayarInput);
                    totalHarga = hargaProduk * kuantitas;

                    if (uangBayar >= totalHarga) {
                        uangKembalian = uangBayar - totalHarga;
                        EdittotalHarga.setText("" + totalHarga);
                        Editkembalian.setText("" + uangKembalian);

                    } else {
                        EdittotalHarga.setText("" + totalHarga);
                        Editkembalian.setText("");
                        Toast.makeText(TambahTransaksiActivity.this, "Uang bayar tidak cukup", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    EdittotalHarga.setText("0");
                    Editkembalian.setText("0");
                    Toast.makeText(TambahTransaksiActivity.this, "Harap masukkan jumlah uang bayar", Toast.LENGTH_SHORT).show();
                    return;
                }
                databaseReference.child("Produk").orderByChild("product_name").equalTo(namaProduk).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            int stok = Integer.parseInt(snapshot.child("stok").getValue(String.class));

                            if (stok <= 0) {
                                Toast.makeText(TambahTransaksiActivity.this, "Stok produk kurang dari 0, transaksi tidak bisa dilakukan", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (stok >= kuantitas) {
                                int sisaStok = stok - kuantitas;
                                snapshot.getRef().child("stok").setValue(String.valueOf(sisaStok));
                            } else {
                                Toast.makeText(TambahTransaksiActivity.this, "Stok produk tidak cukup", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        Transaksi transaksi = new Transaksi(idTransaksi, nameproduk, namePembeli, pricetr, kuantitastr, totalhargatr, uangBayartr, uangKembaliantr, tanggalPembelian);
                        databaseReference.child("Transaksi").child(idTransaksi).setValue(transaksi)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            startActivity(new Intent(TambahTransaksiActivity.this, TransaksiActivity.class));
                                            Toast.makeText(TambahTransaksiActivity.this, "Berhasil Ditambah", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(TambahTransaksiActivity.this, "Gagal menambahkan transaksi", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(TambahTransaksiActivity.this, "Gagal mengurangi stok produk", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TambahTransaksiActivity.this, "Gagal mengambil data transaksi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method untuk mengisi Spinner dengan data produk
    private void populateProdukSpinner() {
        databaseReference.child("Produk").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                produkList.clear();
                produkList.add("Pilih Produk");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String namaProduk = snapshot.child("product_name").getValue(String.class);
                    String harga = snapshot.child("harga").getValue(String.class);
                    produkList.add(namaProduk + " | " + harga);
                }
                produkAdapter = new ArrayAdapter<>(TambahTransaksiActivity.this, android.R.layout.simple_spinner_dropdown_item, produkList);
                spinnerProduk.setAdapter(produkAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TambahTransaksiActivity.this, "Gagal mengambil data produk", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populatePelangganSpinner() {
            databaseReference.child("Pelanggan").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    pelangganList.clear();
                    pelangganList.add("Pilih Pelanggan");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String namap_pelanggan = snapshot.child("namap_pelanggan").getValue(String.class);
                        pelangganList.add(namap_pelanggan);
                    }
                    pelangganAdapter = new ArrayAdapter<>(TambahTransaksiActivity.this, android.R.layout.simple_spinner_dropdown_item, pelangganList);
                    spinnerPelanggan.setAdapter(pelangganAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(TambahTransaksiActivity.this, "Gagal mengambil data pelanggan", Toast.LENGTH_SHORT).show();
                }
            });


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        textViewTanggalPembelian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        TambahTransaksiActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int day, int month, int year) {
                        String[] monthNames = {"Januari", "Februari",
                                "Maret", "April", "Mei", "Juni",
                                "Juli", "Agustus", "September",
                                "Oktober", "November", "Desember"};

                        SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                        try {
                            Date myDate = inFormat.parse(day + "-" + (month + 1) + "-" + year);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

                            String dayName = simpleDateFormat.format(myDate);

                            String tanggalBeli = dayName + ", " + day + " " + monthNames[month] + " " + year;
                            textViewTanggalPembelian.setText(tanggalBeli);

                            tanggalPembelian = year + "-" + (month + 1) + "-" + day;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);

                if (!tanggalPembelian.isEmpty()) {
                    String[] splittedtanggalPembelian = tanggalPembelian.split("[-]");
                    int year = Integer.parseInt(splittedtanggalPembelian[0]);
                    int month = Integer.parseInt(splittedtanggalPembelian[1]) - 1;
                    int day = Integer.parseInt(splittedtanggalPembelian[2]);

                    datePickerDialog.updateDate(year, month, day);
                }

                datePickerDialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TambahTransaksiActivity.this, TransaksiActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(TambahTransaksiActivity.this, TransaksiActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
