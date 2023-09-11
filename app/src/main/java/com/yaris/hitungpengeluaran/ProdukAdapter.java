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

public  class ProdukAdapter extends FirebaseRecyclerAdapter<Produk, ProdukAdapter.PostViewHolder> {

    private Context mContext;
    private String produkId;

    private String generateProdukId() {
        long timestamp = System.currentTimeMillis();
        return String.valueOf(timestamp);
    }
    public ProdukAdapter(@NonNull FirebaseRecyclerOptions<Produk> options, Context context) {
        super(options);
        mContext = context;
        this.produkId = generateProdukId();

    }
    @Override
    protected void onBindViewHolder(@NonNull PostViewHolder holder,final int position, @NonNull final Produk model) {
        holder.items_id.setText(model.getId());
        holder.items_name.setText(model.getProduct_name());
        holder.items_category.setText(model.getDeskripsi());
        holder.items_price.setText(model.getHarga());
        holder.items_stok.setText(model.getStok());
        holder.items_exp.setText(model.getexp());
        holder.items_status.setText(model.getstatus());
        holder.itembarcode.setText(model.getItembarcode());

        if (Integer.parseInt(model.getStok()) < 3) {
            // Tampilkan dialog peringatan
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.items_name.getContext());
            builder.setTitle("Peringatan");
            builder.setMessage("Stok produk " + model.getProduct_name() + " kurang dari 3. Harap perbarui stok produk.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.show();
        }

        String key = getRef(position).getKey();

            holder.delete.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.items_name.getContext());
                builder.setTitle("Hapus data ini?");
                builder.setPositiveButton("Ya, Hapus", (dialogInterface, i) -> {

                    assert key != null;
                    FirebaseDatabase.getInstance().getReference().child("Produk")
                            .child(key).removeValue().addOnCompleteListener(task -> {
                            });
                });
                builder.setNegativeButton("Batal", (dialogInterface, i) -> {
                });
                builder.show();
            });
            holder.card_hasil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FragmentManager manager = ((AppCompatActivity) holder.itemView.getContext()).getSupportFragmentManager();
                    DialogForm dialog = new DialogForm(
                            model.getProduct_name(),
                            model.getDeskripsi(),
                            model.getHarga(),
                            model.getexp(),
                            model.getStok(),
                            model.getstatus(),
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
                inflate(R.layout.item_produk_list,parent,false);
        return new PostViewHolder(v);
    }

    static class PostViewHolder extends RecyclerView.ViewHolder{
        TextView items_id,items_name,items_category,items_price,items_stok,items_status,items_exp,itembarcode;
        ImageView delete;
        CardView card_hasil;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            items_id = itemView.findViewById(R.id.item_id);
            items_name = itemView.findViewById(R.id.item_name);
            items_category = itemView.findViewById(R.id.item_category);
            items_price = itemView.findViewById(R.id.item_price);
            items_stok = itemView.findViewById(R.id.item_stok);
            items_exp = itemView.findViewById(R.id.exp);
            items_status = itemView.findViewById(R.id.item_status);
            itembarcode = itemView.findViewById(R.id.barco);
            delete = itemView.findViewById(R.id.item_delete_button);
            card_hasil = itemView.findViewById(R.id.card_hasil);

        }
    }
    private void showDeleteProdukSuccessDialog(boolean isEmpty) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        if (isEmpty) {
            builder.setTitle("Data Produk Kosong");
            builder.setMessage("Tidak ada produk. Apakah Anda ingin menambahkan produk?");
            builder.setPositiveButton("Ya, Tambahkan", (dialog, which) -> {
                mContext.startActivity(new Intent(mContext, TambahProdukActivity.class));
            });
            builder.setNegativeButton("Tidak", (dialog, which) -> {
            });
        } else {
            builder.setTitle("Produk berhasil dihapus");
            builder.setPositiveButton("OK", (dialog, which) -> {
                Toast.makeText(mContext, "Produk berhasil dihapus", Toast.LENGTH_SHORT).show();
            });
        }
        builder.show();
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();

        if (getItemCount() == 0) {
            showDeleteProdukSuccessDialog(true);
        }
    }
}
