package com.yaris.hitungpengeluaran;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DialogForm2 extends DialogFragment {

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

    String items_namePembelitr,items_totalhargatr,  items_uangBayartr,uangKembaliantr,key,pilih;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public DialogForm2(String items_namePembelitr, String items_totalhargatr, String items_uangBayartr,String uangKembaliantr, String key, String pilih) {

        this.items_namePembelitr = items_namePembelitr;
        this.items_totalhargatr = items_totalhargatr;
        this.items_uangBayartr = items_uangBayartr;
        this.uangKembaliantr = uangKembaliantr;
        this.key = key;
        this.pilih = pilih;
    }
    EditText item_namepembeli,total_harga,item_uangbayar,uang_kembalian;
    Button btnCatatTransaksi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.activity_editdatatransaksictivity, container,false);
        item_namepembeli = view.findViewById(R.id.editnamapembeli);
        total_harga = view.findViewById(R.id.edittotalharga);
        item_uangbayar = view.findViewById(R.id.editbayar);
        uang_kembalian = view.findViewById(R.id.editkembalian);
        btnCatatTransaksi = view.findViewById(R.id.updatetr);

        item_namepembeli.setText(items_namePembelitr);
        total_harga.setText(items_totalhargatr);
        item_uangbayar.setText(items_uangBayartr);
        uang_kembalian.setText(uangKembaliantr);

        btnCatatTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedNamaPembeli = item_namepembeli.getText().toString().trim();
                String updatedTotal = total_harga.getText().toString().trim();
                String updatedUangBayar = item_uangbayar.getText().toString().trim();
                String updatedKembalian = uang_kembalian.getText().toString().trim();

                if (updatedNamaPembeli.isEmpty() || updatedTotal.isEmpty() || updatedUangBayar.isEmpty() || updatedKembalian.isEmpty()) {
                    showDialog("Peringatan", "Mohon isi semua kolom sebelum menyimpan.");
                } else if (pilih.equals("ubah")) {
                    boolean success = updateData(updatedNamaPembeli, updatedTotal, updatedUangBayar,updatedKembalian);
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
    private boolean updateData(String updatedNamaPembeli, String updatedTotal, String updatedUangBayar,String updatedKembalian) {
        try {
            DatabaseReference itemRef = database.child("Transaksi").child(key);

        itemRef.child("namePembeli").setValue(updatedNamaPembeli);
        itemRef.child("totalharga").setValue(updatedTotal);
        itemRef.child("uangbayar").setValue(updatedUangBayar);
        itemRef.child("uangKembalian").setValue(updatedKembalian);
        return true; // Update success
    } catch (Exception e) {
        e.printStackTrace();
        return false; // Update failed
    }
}

    // Method to show Toast message
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}