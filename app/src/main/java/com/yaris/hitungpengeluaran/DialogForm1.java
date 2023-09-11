package com.yaris.hitungpengeluaran;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DialogForm1 extends DialogFragment {

    private void showDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    String items_keterangan, items_jumlahProduk,items_tanggal, items_jumlahUang, items_sumber,key, pilih;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public DialogForm1(String items_keterangan, String items_tanggal, String items_jumlahProduk,String items_jumlahUang, String items_sumber,String key, String pilih) {
        this.items_keterangan = items_keterangan;
        this.items_tanggal = items_tanggal;
        this.items_jumlahUang = items_jumlahUang;
        this.items_jumlahProduk = items_jumlahProduk;
        this.items_sumber = items_sumber;

        this.key = key;
        this.pilih = pilih;
    }
    EditText item_keterangan,item_tanggal,item_jumlahuang, item_jumlah, item_sumber;
    Button btn_simpan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.activity_editpengeluaranctivity, container,false);

        item_keterangan = view.findViewById(R.id.editketerangan);
        item_tanggal = view.findViewById(R.id.edittanggal);
        item_jumlahuang = view.findViewById(R.id.editjumlahuang);
        item_jumlah = view.findViewById(R.id.editjumlahproduk);
        item_sumber = view.findViewById(R.id.editsumber);


        btn_simpan = view.findViewById(R.id.updatepengeluaran);

        item_keterangan.setText(items_keterangan);
        item_tanggal.setText(items_tanggal);
        item_jumlahuang.setText(items_jumlahUang);
        item_jumlah.setText(items_jumlahProduk);
        item_sumber.setText(items_sumber);

        item_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updateketerangan = item_keterangan.getText().toString().trim();
                String updatedtanggal = item_tanggal.getText().toString().trim();
                String updatedjumlahuang = item_jumlahuang.getText().toString().trim();
                String updatejumlahProduk = item_jumlah.getText().toString().trim();
                String updatesumber = item_sumber.getText().toString().trim();


                if (updateketerangan.isEmpty() || updatedtanggal.isEmpty() || updatedjumlahuang.isEmpty() || updatejumlahProduk.isEmpty() || updatesumber.isEmpty()) {
                    showDialog("Peringatan", "Mohon isi semua kolom sebelum menyimpan.");
                } else if (pilih.equals("ubah")) {
                    boolean success = updateData(updatejumlahProduk,updateketerangan, updatedtanggal, updatedjumlahuang, updatesumber);
                    if (success) {
                        showToast("Data Berhasil Di Update!");
                    } else {
                        showToast("Data Gagal Di Update.");
                    }
                }
            }
        });
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        return view;
    }
    private boolean updateData(String updatejumlahProduk,String updateketerangan, String updatedtanggal, String updatedjumlahuang, String updatesumber) {
        try {
            DatabaseReference itemRef = database.child("Pengeluaran").child(key);

        itemRef.child("keterangan").setValue(updateketerangan);
        itemRef.child("tanggal").setValue(updatedtanggal);
        itemRef.child("jumlahUang").setValue(updatedjumlahuang);
        itemRef.child("jumlahProduk").setValue(updatejumlahProduk);
        itemRef.child("sumber").setValue(updatesumber);

            return true;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
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

}
