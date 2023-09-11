package com.yaris.hitungpengeluaran;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.yaris.hitungpengeluaran.R;

public class AboutActivity extends AppCompatActivity {

    Toolbar toolbar;
//    ImageButton askAbout;
    TextView bantuan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        bantuan = findViewById(R.id.bantuan);

        toolbar = findViewById(R.id.toolbarabout);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Letakkan bagian ini di dalam onCreate
        bantuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke halaman pendaftaran
                Intent intent = new Intent(AboutActivity.this, BantuanActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(AboutActivity.this, HomeActivity.class));
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(AboutActivity.this, HomeActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}