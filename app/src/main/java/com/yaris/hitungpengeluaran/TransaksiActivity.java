package com.yaris.hitungpengeluaran;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import io.reactivex.rxjava3.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class TransaksiActivity extends AppCompatActivity {

    FloatingActionButton transaksibtn;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    RecyclerView recyclerViewtr;
    TransaksiAdapter transaksiAdapter;// Deklarasi adapter sebagai atribut
    TextView tvTotal,tvTotalprod ;

    Toolbar toolbar;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        Toolbar toolbarsearch = findViewById(R.id.toolbartr);
        setSupportActionBar(toolbarsearch);
        getSupportActionBar().setTitle("");

        recyclerViewtr = findViewById(R.id.recycle_viewTransaksi);
        recyclerViewtr.setLayoutManager(new LinearLayoutManager(this));

        toolbar = findViewById(R.id.toolbarlaporantransaksi);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FirebaseRecyclerOptions<Transaksi> options =
                new FirebaseRecyclerOptions.Builder<Transaksi>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Transaksi"), Transaksi.class)
                        .build();

        transaksiAdapter = new TransaksiAdapter(options, this);
        recyclerViewtr.setAdapter(transaksiAdapter);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Transaksi");

        tvTotal = findViewById(R.id.tvTotalpem);
        tvTotalprod = findViewById(R.id.tvTotalprod);
        calculateAndDisplayTotal();
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

    }

        private void calculateAndDisplayTotal() {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int totalJumlah = 0;
                    int totalProduk = 0; // Inisialisasi variabel totalProduk

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Transaksi transaksi = dataSnapshot.getValue(Transaksi.class);

                        String totalhargaString = transaksi.gettotalharga().trim();
                        int totalharga = Integer.parseInt(totalhargaString);
                        totalJumlah += totalharga;

                        int kuantitas = Integer.parseInt(transaksi.getkuantitas()); // Anggap Anda memiliki method untuk mendapatkan kuantitas
                        totalProduk += kuantitas; // Akumulasi kuantitas
                    }

                    tvTotal.setText("Rp." + totalJumlah);
                    tvTotalprod.setText(String.valueOf(totalProduk)); // Tampilkan total produk
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(TransaksiActivity.this, "Terjadi kesalahan: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            try {
            } catch (Exception e) {
                // Tangani kesalahan di sini
                e.printStackTrace();
                Toast.makeText(TransaksiActivity.this, "Terjadi kesalahan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        transaksibtn = findViewById(R.id.btnTambahTransaksi);
        transaksibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TransaksiActivity.this, TambahTransaksiActivity.class));
            }
        });

        FirebaseRecyclerOptions<Transaksi> options =
                new FirebaseRecyclerOptions.Builder<Transaksi>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Transaksi"), Transaksi.class)
                        .build();

        transaksiAdapter = new TransaksiAdapter(options, this);
        recyclerViewtr.setAdapter(transaksiAdapter);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TransaksiActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(TransaksiActivity.this, HomeActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void refreshData() {
        transaksiAdapter.stopListening();
        calculateAndDisplayTotal();
        transaksiAdapter.startListening();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        transaksiAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.searchId);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Ketik di sini untuk mencari");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                mysearch(newText);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mysearch(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void mysearch(String newText) {
        FirebaseRecyclerOptions<Transaksi> options =
                new FirebaseRecyclerOptions.Builder<Transaksi>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Transaksi").orderByChild("namePembeli").startAt(newText).endAt(newText + "\uf8ff"), Transaksi.class)
                        .build();

        transaksiAdapter = new TransaksiAdapter(options, this);
        transaksiAdapter.startListening();
        recyclerViewtr.setAdapter(transaksiAdapter);
    }
}
