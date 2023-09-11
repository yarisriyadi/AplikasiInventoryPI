package com.yaris.hitungpengeluaran;

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

public class PelangganActivity extends AppCompatActivity {

    FloatingActionButton btnTambahPelanggan;
    FirebaseAuth mAuth;
    Toolbar toolbar;

    DatabaseReference databaseReference;
    RecyclerView recyclerViewpelanggan;
    PelangganAdapter PelangganAdapter; // Deklarasi adapter sebagai atribut

    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelanggan);

        Toolbar toolbarsearch = findViewById(R.id.toolbarpelanggan);
        setSupportActionBar(toolbarsearch);
        getSupportActionBar().setTitle("");

        recyclerViewpelanggan = findViewById(R.id.recycle_viewPelanggan);
        recyclerViewpelanggan.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Pelanggan");


        toolbar = findViewById(R.id.toolbarlaporanpelanggan);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        btnTambahPelanggan = findViewById(R.id.btnTambahPelanggan);
        btnTambahPelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PelangganActivity.this, TambahPelangganActivity.class));
            }
        });

        FirebaseRecyclerOptions<Pelanggan> options =
                new FirebaseRecyclerOptions.Builder<Pelanggan>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Pelanggan"), Pelanggan.class)
                        .build();

        PelangganAdapter = new PelangganAdapter(options, this);
        recyclerViewpelanggan.setAdapter(PelangganAdapter);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PelangganActivity.this, HomeActivity.class));
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(PelangganActivity.this, HomeActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshData() {
        PelangganAdapter.stopListening();
        PelangganAdapter.startListening();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        PelangganAdapter.startListening();
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
            FirebaseRecyclerOptions < Pelanggan > options =
                    new FirebaseRecyclerOptions.Builder<Pelanggan>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Pelanggan").orderByChild("namap_pelanggan").startAt(newText).endAt(newText + "\uf8ff"), Pelanggan.class)
                            .build();

        PelangganAdapter = new PelangganAdapter(options, this);
        PelangganAdapter.startListening();
        recyclerViewpelanggan.setAdapter(PelangganAdapter);
        }

    }