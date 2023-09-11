package com.yaris.hitungpengeluaran;

public class Pelanggan {
    private String id, namap_pelanggan,alamat,no_hp;

    public Pelanggan() { }
    public Pelanggan(String id , String namap_pelanggan, String alamat, String no_hp){

        this.id = id;
        this.namap_pelanggan = namap_pelanggan;
        this.alamat = alamat;
        this.no_hp = no_hp;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getnamap_pelanggan() {
        return namap_pelanggan;
    }
    public String getalamat() {return alamat;}
    public String getno_hp() {return no_hp;}
}
