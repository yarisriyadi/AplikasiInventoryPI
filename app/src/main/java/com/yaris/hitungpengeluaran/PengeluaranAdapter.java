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

public  class PengeluaranAdapter extends FirebaseRecyclerAdapter<Pengeluaran, PengeluaranAdapter.PostViewHolder> {
    private Context mContext;
    private String pengeluaranId;

    private String generatePengeluaranId() {
        long timestamp = System.currentTimeMillis();
        return String.valueOf(timestamp);
}
    public PengeluaranAdapter(@NonNull FirebaseRecyclerOptions<Pengeluaran> options, Context context) {
        super(options);
        mContext = context;
        this.pengeluaranId = generatePengeluaranId();

    }
    private int getJumlahPengeluaran() {
        return getItemCount();
    }

    @Override
    protected void onBindViewHolder(@NonNull PostViewHolder holder, final int position, @NonNull final Pengeluaran model) {

        holder.items_id.setText(model.getId());
        holder.items_keterangan.setText(model.getketerangan());
        holder.items_tanggal.setText(model.gettanggal());
        holder.items_jumlahProduk.setText(model.getjumlahProduk());
        holder.items_jumlahUang.setText(model.getjumlahUang());
        holder.items_sumber.setText(model.getsumber());


        String key = getRef(position).getKey();


        holder.deletepengeluaran.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
            builder.setTitle("Hapus data ini?");
            builder.setPositiveButton("Ya, Hapus", (dialogInterface, i) -> {

                assert key != null;
                FirebaseDatabase.getInstance().getReference().child("Pengeluaran")
                        .child(key).removeValue().addOnCompleteListener(task -> {
                        });
            });
            builder.setNegativeButton("Batal", (dialogInterface, i) -> {
            });
            builder.show();
        });
        holder.card_pengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager manager = ((AppCompatActivity) holder.itemView.getContext()).getSupportFragmentManager();
                DialogForm1 dialog = new DialogForm1(

                        model.getketerangan(),
                        model.gettanggal(),
                        model.getjumlahProduk(),
                        model.getjumlahUang(),
                        model.getsumber(),
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
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_pengeluaran_list, parent, false);
        return new PostViewHolder(v);
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView items_id,items_jumlahProduk,items_keterangan, items_tanggal, items_jumlahUang, items_sumber;
        ImageView deletepengeluaran;
        CardView card_pengeluaran;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            items_id = itemView.findViewById(R.id.item_id);
            items_keterangan = itemView.findViewById(R.id.item_keterangan);
            items_jumlahProduk = itemView.findViewById(R.id.item_jumlah);
            items_tanggal = itemView.findViewById(R.id.item_tanggal);
            items_jumlahUang = itemView.findViewById(R.id.item_jumlahUang);
            items_sumber = itemView.findViewById(R.id.item_sumber);
            deletepengeluaran = itemView.findViewById(R.id.item_delete_pengeluaran);
            card_pengeluaran = itemView.findViewById(R.id.card_pengeluaran);

        }
    }

    private void showDeletePengeluaranSuccessDialog(boolean isEmpty) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        if (isEmpty) {
            builder.setTitle("Data Pengeluaran Kosong");
            builder.setMessage("Tidak ada pengeluaran. Apakah Anda ingin menambahkan suatu pengeluaran?");
            builder.setPositiveButton("Ya, Tambahkan", (dialog, which) -> {
                mContext.startActivity(new Intent(mContext, TambahPengeluaranActivity.class));
            });
            builder.setNegativeButton("Tidak", (dialog, which) -> {
            });
        } else {
            builder.setTitle("Pengeluaran berhasil dihapus");
            builder.setPositiveButton("OK", (dialog, which) -> {
                Toast.makeText(mContext, "Pengeluaran berhasil dihapus", Toast.LENGTH_SHORT).show();
            });
        }
        builder.show();
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();

        if (getItemCount() == 0) {
            showDeletePengeluaranSuccessDialog(true);
        }
    }
}