package com.yaris.hitungpengeluaran;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.rxjava3.annotations.NonNull;

public class PengeluaranActivity extends AppCompatActivity {

    FloatingActionButton btnTambahPengeluaran;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    RecyclerView recyclerViewpengeluaran;
    PengeluaranAdapter pengeluaranAdapter; // Deklarasi adapter sebagai atribut

    TextView tvTotal;

    Toolbar toolbar;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengeluaran);

        Toolbar toolbarsearch = findViewById(R.id.toolbarpengeluaran);
        setSupportActionBar(toolbarsearch);
        getSupportActionBar().setTitle("");

        recyclerViewpengeluaran = findViewById(R.id.recycle_viewPengeluaran);
        recyclerViewpengeluaran.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        tvTotal = findViewById(R.id.tvTotal);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Pengeluaran");

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
                int total = 0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Pengeluaran pengeluaran = dataSnapshot.getValue(Pengeluaran.class);
                    int jumlahUang = Integer.parseInt(pengeluaran.getjumlahUang());
                    total += jumlahUang;
                }

                tvTotal.setText("Rp." + total);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if needed
            }
        });

        toolbar = findViewById(R.id.toolbarlaporanpengeluaran);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        btnTambahPengeluaran = findViewById(R.id.btnTambahPengeluaran);
        btnTambahPengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PengeluaranActivity.this, TambahPengeluaranActivity.class));
            }
        });

        FirebaseRecyclerOptions<Pengeluaran> options =
                new FirebaseRecyclerOptions.Builder<Pengeluaran>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Pengeluaran"), Pengeluaran.class)
                        .build();

        pengeluaranAdapter = new PengeluaranAdapter(options, this);
        recyclerViewpengeluaran.setAdapter(pengeluaranAdapter);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PengeluaranActivity.this, HomeActivity.class));
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(PengeluaranActivity.this, HomeActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshData() {
        pengeluaranAdapter.stopListening();
        calculateAndDisplayTotal();
        pengeluaranAdapter.startListening();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        pengeluaranAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){

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
    private void mysearch (String newText) {
            FirebaseRecyclerOptions < Pengeluaran > options =
                    new FirebaseRecyclerOptions.Builder<Pengeluaran>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Pengeluaran").orderByChild("sumber").startAt(newText).endAt(newText + "\uf8ff"), Pengeluaran.class)
                            .build();

        pengeluaranAdapter = new PengeluaranAdapter(options, this);
        pengeluaranAdapter.startListening();
        recyclerViewpengeluaran.setAdapter(pengeluaranAdapter);
        }
    }