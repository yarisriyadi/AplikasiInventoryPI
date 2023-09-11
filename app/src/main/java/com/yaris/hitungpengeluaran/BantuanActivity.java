package com.yaris.hitungpengeluaran;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BantuanActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageButton askAbout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bantuan);


        askAbout = findViewById(R.id.ask_about);
        askAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendEmail = new Intent(Intent.ACTION_SEND);
                sendEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{"yarisriyadi618@gmail.com"});
                sendEmail.putExtra(Intent.EXTRA_SUBJECT, "Bug Pada Aplikasi InvenSmart");
                sendEmail.putExtra(Intent.EXTRA_TEXT, "Saya menemukan bug");
                sendEmail.setType("message/rfc822");
                startActivity(sendEmail);
            }
        });

        toolbar = findViewById(R.id.toolbarbantuan);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(BantuanActivity.this, AboutActivity.class));
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(BantuanActivity.this, AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}