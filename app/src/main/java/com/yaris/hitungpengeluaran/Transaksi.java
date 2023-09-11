package com.yaris.hitungpengeluaran;

public class Transaksi {
    public String id, nameproduk, namePembeli,pricetr, kuantitas, totalharga,uangbayar , uangKembalian, created_at;

    public Transaksi() { }

    public Transaksi(String id, String nameproduk,String namePembeli,String pricetr,String kuantitas,String totalharga,String uangbayar,String uangKembalian, String created_at ) {

        this.id = id;
        this.nameproduk = nameproduk;
        this.namePembeli = namePembeli;
        this.pricetr = pricetr;
        this.kuantitas = kuantitas;
        this.totalharga = totalharga;
        this.uangbayar = uangbayar;
        this.uangKembalian = uangKembalian;
        this.created_at = created_at;

    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getnameproduk() {return nameproduk;}
    public String getnamePembeli() {return namePembeli;}
    public String pricetr() {return pricetr;}
    public String getkuantitas() {return kuantitas;}
    public String gettotalharga() {return this.totalharga;}
    public String getuangbayar() {return uangbayar;}
    public String getuangKembalian() {return this.uangKembalian;}
    public String getcreated_at() {return created_at;}
}

