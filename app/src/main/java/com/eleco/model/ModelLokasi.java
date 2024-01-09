package com.eleco.model;

public class ModelLokasi {
    private String nama;
    private String deskripsi;

    public ModelLokasi(String nama, String deskripsi) {
        this.nama = nama;
        this.deskripsi = deskripsi;
    }

    public String getNama() {
        return nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

}
