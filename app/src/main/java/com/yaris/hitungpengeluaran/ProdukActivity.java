package com.yaris.hitungpengeluaran;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.rxjava3.annotations.NonNull;

public class ProdukActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProdukAdapter adapter;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FloatingActionButton btnTambahBarang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk);

        Toolbar toolbarsearch = findViewById(R.id.toolbar);
        setSupportActionBar(toolbarsearch);
        getSupportActionBar().setTitle("");

        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Produk");

        toolbar = findViewById(R.id.toolbarstok);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        btnTambahBarang = findViewById(R.id.btnTambahBarang);
        btnTambahBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProdukActivity.this, TambahProdukActivity.class));
            }
        });


        FirebaseRecyclerOptions<Produk> options =
                new FirebaseRecyclerOptions.Builder<Produk>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Produk"), Produk.class)
                        .build();

        adapter = new ProdukAdapter(options, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ProdukActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(ProdukActivity.this, HomeActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
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
        searchView.setQueryHint("Ketik di sini untuk mencari produk");

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
    private void mysearch(String str) {
        FirebaseRecyclerOptions<Produk> options =
                new FirebaseRecyclerOptions.Builder<Produk>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Produk").orderByChild("product_name").startAt(str).endAt(str + "\uf8ff"), Produk.class)
                        .build();

        adapter = new ProdukAdapter(options, this);
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }
}