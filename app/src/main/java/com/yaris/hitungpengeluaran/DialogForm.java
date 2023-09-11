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

public class DialogForm extends DialogFragment {

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

    String items_name, items_category, items_price, items_exp,items_stok, items_status,key, pilih;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public DialogForm(String items_name, String items_category, String items_price, String items_exp,String items_stok, String items_status,String key, String pilih) {
        this.items_name = items_name;
        this.items_category = items_category;
        this.items_price = items_price;
        this.items_exp = items_exp;
        this.items_status = items_status;
        this.items_stok = items_stok;
        this.key = key;
        this.pilih = pilih;
    }
    EditText item_name,item_cat,item_price,item_exp,item_stok, item_status;
    Button btn_simpan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.activity_editdatactivity, container,false);

        item_name = view.findViewById(R.id.editname);
        item_cat = view.findViewById(R.id.editdeskripsi);
        item_price = view.findViewById(R.id.editharga);
        item_exp= view.findViewById(R.id.editexp);
        item_stok = view.findViewById(R.id.editustok);
        item_status = view.findViewById(R.id.editstatus);
        btn_simpan = view.findViewById(R.id.update);

        item_name.setText(items_name);
        item_cat.setText(items_category);
        item_price.setText(items_price);
        item_exp.setText(items_exp);
        item_stok.setText(items_stok);
        item_status.setText(items_status);

        item_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedName = item_name.getText().toString().trim();
                String updatedCategory = item_cat.getText().toString().trim();
                String updatedPrice = item_price.getText().toString().trim();
                String updatedexp = item_exp.getText().toString().trim();
                String updatedStok = item_stok.getText().toString().trim();
                String updatedStatus= item_status.getText().toString().trim();


                if (updatedName.isEmpty() || updatedCategory.isEmpty() || updatedPrice.isEmpty() || updatedStok.isEmpty() || updatedexp.isEmpty() || updatedStatus.isEmpty()) {
                    showDialog("Peringatan", "Mohon isi semua kolom sebelum menyimpan.");
                } else if (pilih.equals("ubah")) {
                    boolean success = updateData(updatedName, updatedCategory, updatedPrice,updatedStok, updatedexp, updatedStatus);
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
    private boolean updateData(String updatedName, String updatedCategory, String updatedPrice, String updatedStok, String updatedexp, String updatedStatus) {
        try {
            DatabaseReference itemRef = database.child("Produk").child(key);

        itemRef.child("product_name").setValue(updatedName);
        itemRef.child("deskripsi").setValue(updatedCategory);
        itemRef.child("harga").setValue(updatedPrice);
        itemRef.child("exp").setValue(updatedexp);
        itemRef.child("stok").setValue(updatedStok);
        itemRef.child("status").setValue(updatedStatus);
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
                        item_exp.setText(selectedDate);
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }
}