package com.yaris.hitungpengeluaran;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class TransaksiAdapter extends FirebaseRecyclerAdapter<Transaksi, TransaksiAdapter.PostViewHolder> {
    private Context mContext;
    private String transaksiId;

    private String generateTransaksiId() {

        long timestamp = System.currentTimeMillis();
        return String.valueOf(timestamp);
    }

    public TransaksiAdapter(@NonNull FirebaseRecyclerOptions<Transaksi> options, Context context) {
        super(options);
        mContext = context;
        this.transaksiId = generateTransaksiId();
    }

    @Override
    protected void onBindViewHolder(@NonNull PostViewHolder holder, final int position, @NonNull final Transaksi model) {
        // Ambil data dari model dan pasangkan ke TextView di dalam CardView

        holder.items_id.setText(model.getId());
        holder.items_nameproduk.setText(model.getnameproduk());
        holder.items_namePembelitr.setText(model.getnamePembeli());
        holder.items_pricetr.setText(model.pricetr());
        holder.items_kuantitastr.setText(model.getkuantitas());
        holder.items_totalhargatr.setText(model.gettotalharga());
        holder.items_uangBayartr.setText(model.getuangbayar());
        holder.uangKembaliantr.setText(model.getuangKembalian());
        holder.created_at.setText(model.getcreated_at());

        String key = getRef(position).getKey();

        holder.deletetr.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
            builder.setTitle("Hapus data ini?");
            builder.setPositiveButton("Ya, Hapus", (dialogInterface, i) -> {
                assert key != null;
                FirebaseDatabase.getInstance().getReference().child("Transaksi")
                        .child(key).removeValue().addOnCompleteListener(task -> {
                        });
            });
            builder.setNegativeButton("Batal", (dialogInterface, i) -> {
            });
            builder.show();
        });

        holder.card_transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = ((AppCompatActivity) holder.itemView.getContext()).getSupportFragmentManager();
                DialogForm2 dialog = new DialogForm2(
                        model.getnamePembeli(),
                        model.gettotalharga(),
                        model.getuangbayar(),
                        model.getuangKembalian(),
                        key,
                        "ubah"
                );
                dialog.show(manager, "UpdateDialog");
            }
        });
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaksi_list, parent, false);
        return new PostViewHolder(v);
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView items_id,items_nameproduk, items_namePembelitr, items_pricetr, items_kuantitastr, items_totalhargatr, items_uangBayartr, uangKembaliantr, created_at;
        ImageView deletetr;
        CardView card_transaksi;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            items_id = itemView.findViewById(R.id.item_id);
            items_nameproduk = itemView.findViewById(R.id.item_nameproduk);
            items_namePembelitr = itemView.findViewById(R.id.item_namepembeli);
            items_pricetr = itemView.findViewById(R.id.item_price2);
            items_kuantitastr = itemView.findViewById(R.id.item_kuantitas);
            items_totalhargatr = itemView.findViewById(R.id.total_harga);
            items_uangBayartr = itemView.findViewById(R.id.uang_bayar);
            uangKembaliantr = itemView.findViewById(R.id.uang_kembalian);
            created_at = itemView.findViewById(R.id.tanggalPembelian);
            deletetr = itemView.findViewById(R.id.item_delete_buttontr);
            card_transaksi = itemView.findViewById(R.id.card_transaksi);
        }
    }

    private void showDeleteTransaksiSuccessDialog(boolean isEmpty) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        if (isEmpty) {
            builder.setTitle("Data Transaksi Kosong");
            builder.setMessage("Tidak ada data transaksi. Apakah Anda ingin menambahkan transaksi baru?");
            builder.setPositiveButton("Ya, Tambahkan", (dialog, which) -> {
                mContext.startActivity(new Intent(mContext, TambahTransaksiActivity.class));
            });
            builder.setNegativeButton("Tidak", (dialog, which) -> {
            });
        } else {
            builder.setTitle("Transaksi berhasil dihapus");
            builder.setPositiveButton("OK", (dialog, which) -> {
                Toast.makeText(mContext, "Transaksi berhasil dihapus", Toast.LENGTH_SHORT).show();
            });
        }
        builder.show();
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();

        if (getItemCount() == 0) {
            showDeleteTransaksiSuccessDialog(true);
        }
    }
}
