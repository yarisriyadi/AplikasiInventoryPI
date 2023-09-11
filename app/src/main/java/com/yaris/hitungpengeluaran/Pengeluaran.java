package com.yaris.hitungpengeluaran;

public class Pengeluaran {
    public String id,jumlahProduk,keterangan, tanggal,jumlahUang,sumber;

    public Pengeluaran() { }

    // Buat constructor untuk mengisi data pada atribut
    public Pengeluaran(String id,String jumlahProduk, String keterangan, String tanggal, String jumlahUang, String sumber) {

        this.id = id;
        this.keterangan = keterangan;
        this.tanggal = tanggal;
        this.jumlahProduk = jumlahProduk;
        this.jumlahUang = jumlahUang;
        this.sumber = sumber;

    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getketerangan() {return keterangan;}
    public String getjumlahProduk() {return jumlahProduk;}
    public String gettanggal() {return tanggal;}
    public String getjumlahUang() {return jumlahUang;}
    public String getsumber() {return sumber;}
}

