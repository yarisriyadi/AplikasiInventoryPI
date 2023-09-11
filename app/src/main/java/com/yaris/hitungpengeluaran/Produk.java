package com.yaris.hitungpengeluaran;

public class Produk {
    private String id, product_name,deskripsi,harga,stok,exp,itembarcode, status;

    public Produk() { }
    public Produk(String id ,String product_name, String deskripsi, String harga, String stok ,String exp,String itembarcode, String status){

        this.id = id;
        this.product_name = product_name;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.stok = stok;
        this.exp = exp;
        this.status = status;
        this.itembarcode = itembarcode;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getProduct_name() {
        return product_name;
    }
    public String getDeskripsi() {return deskripsi;}
    public String getHarga() {return harga;}
    public String getStok() {return stok;}
    public String getexp() {return exp;}
    public String getstatus() {return status;}
    public String getItembarcode() {
        return itembarcode;
    }
}
