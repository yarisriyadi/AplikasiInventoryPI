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

public class DialogForm3 extends DialogFragment {

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

    String items_namepelanggan, items_alamat, items_nohp, key, pilih;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public DialogForm3(String items_namepelanggan, String items_alamat, String items_nohp, String key, String pilih) {
        this.items_namepelanggan = items_namepelanggan;
        this.items_alamat = items_alamat;
        this.items_nohp = items_nohp;
        this.key = key;
        this.pilih = pilih;
    }
    EditText item_namepelanggan,item_alamat,item_nohp;
    Button btn_simpan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.activity_editpelangganctivity, container,false);

        item_namepelanggan = view.findViewById(R.id.editpelanggan);
        item_alamat = view.findViewById(R.id.editalamat);
        item_nohp = view.findViewById(R.id.editnohp);

        btn_simpan = view.findViewById(R.id.updatepelanggan);

        item_namepelanggan.setText(items_namepelanggan);
        item_alamat.setText(items_alamat);
        item_nohp.setText(items_nohp);

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatednamepelanggan = item_namepelanggan.getText().toString().trim();
                String updatedalamat = item_alamat.getText().toString().trim();
                String updatednohp = item_nohp.getText().toString().trim();

                if (updatednamepelanggan.isEmpty() || updatedalamat.isEmpty() || updatednohp.isEmpty()) {
                    showDialog("Peringatan", "Mohon isi semua kolom sebelum menyimpan.");
                } else if (pilih.equals("ubah")) {
                    boolean success = updateData(updatednamepelanggan, updatedalamat, updatednohp);
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
    private boolean updateData(String updatednamepelanggan, String updatedalamat, String updatednohp) {
        try {
            DatabaseReference itemRef = database.child("Pelanggan").child(key);

        itemRef.child("namap_pelanggan").setValue(updatednamepelanggan);
        itemRef.child("alamat").setValue(updatedalamat);
        itemRef.child("no_hp").setValue(updatednohp);
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