package com.yaris.hitungpengeluaran;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public  class PelangganAdapter extends FirebaseRecyclerAdapter<Pelanggan, PelangganAdapter.PostViewHolder> {

    private Context mContext;
    private String pelangganId; // Deklarasikan variabel transaksiId

    private String generatePelangganId() {
        // Menghasilkan ID berdasarkan timestamp
        long timestamp = System.currentTimeMillis();
        return String.valueOf(timestamp);
    }
    public PelangganAdapter(@NonNull FirebaseRecyclerOptions<Pelanggan> options, Context context) {
        super(options);
        mContext = context;
        this.pelangganId = generatePelangganId();

    }
    @Override
    protected void onBindViewHolder(@NonNull PostViewHolder holder,final int position, @NonNull final Pelanggan model) {
        holder.items_id.setText(model.getId());
        holder.items_namepelanggan.setText(model.getnamap_pelanggan());
        holder.items_alamat.setText(model.getalamat());
        holder.items_nohp.setText(model.getno_hp());

        String key = getRef(position).getKey();


        holder.item_delete_pelanggan.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
            builder.setTitle("Hapus data ini?");
            builder.setPositiveButton("Ya, Hapus", (dialogInterface, i) -> {

                assert key != null;
                FirebaseDatabase.getInstance().getReference().child("Pelanggan")
                        .child(key).removeValue().addOnCompleteListener(task -> {
                        });
            });
            builder.setNegativeButton("Batal", (dialogInterface, i) -> {
            });
            builder.show();
        });
        holder.card_pelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager manager = ((AppCompatActivity) holder.itemView.getContext()).getSupportFragmentManager();
                DialogForm3 dialog = new DialogForm3(

                        model.getnamap_pelanggan(),
                        model.getalamat(),
                        model.getno_hp(),
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
                inflate(R.layout.item_pelanggan_list,parent,false);
        return new PostViewHolder(v);
    }

    static class PostViewHolder extends RecyclerView.ViewHolder{
        TextView items_id,items_namepelanggan,items_alamat,items_nohp;
        ImageView item_delete_pelanggan;
        CardView card_pelanggan;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            items_id = itemView.findViewById(R.id.item_id);
            items_namepelanggan = itemView.findViewById(R.id.item_namapelanggan);
            items_alamat = itemView.findViewById(R.id.item_alamat);
            items_nohp = itemView.findViewById(R.id.item_nohp);
            card_pelanggan = itemView.findViewById(R.id.card_pelanggan);
            item_delete_pelanggan = itemView.findViewById(R.id.item_delete_pelanggan);



        }
    }
}
