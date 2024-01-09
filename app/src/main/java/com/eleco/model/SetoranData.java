package com.eleco.model;

import java.util.ArrayList;

public class SetoranData {
    private String idSetoran;
    private String namaLokasi;
    private String deskripsiLokasi;
    private int poin;
    private ArrayList<String> jenisItems;
    private ArrayList<Integer> jumlahItems;
    private long timestamp; // Menambah field timestamp

    // Konstruktor kosong diperlukan untuk Firebase
    public SetoranData() {
    }

    public SetoranData(String idSetoran, String namaLokasi, String deskripsiLokasi, int poin,
                       ArrayList<String> jenisItems, ArrayList<Integer> jumlahItems) {
        this.idSetoran = idSetoran;
        this.namaLokasi = namaLokasi;
        this.deskripsiLokasi = deskripsiLokasi;
        this.poin = poin;
        this.jenisItems = jenisItems;
        this.jumlahItems = jumlahItems;
        this.timestamp = System.currentTimeMillis(); // Mengisi nilai timestamp dengan waktu saat ini
    }

    // Getter dan Setter lainnya

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getIdSetoran() {
        return idSetoran;
    }

    public void setIdSetoran(String idSetoran) {
        this.idSetoran = idSetoran;
    }

    public String getNamaLokasi() {
        return namaLokasi;
    }

    public void setNamaLokasi(String namaLokasi) {
        this.namaLokasi = namaLokasi;
    }

    public String getDeskripsiLokasi() {
        return deskripsiLokasi;
    }

    public void setDeskripsiLokasi(String deskripsiLokasi) {
        this.deskripsiLokasi = deskripsiLokasi;
    }

    public int getpoin() {
        return poin;
    }

    public void setpoin(int poin) {
        this.poin = poin;
    }

    public ArrayList<String> getJenisItems() {
        return jenisItems;
    }

    public void setJenisItems(ArrayList<String> jenisItems) {
        this.jenisItems = jenisItems;
    }

    public ArrayList<Integer> getJumlahItems() {
        return jumlahItems;
    }

    public void setJumlahItems(ArrayList<Integer> jumlahItems) {
        this.jumlahItems = jumlahItems;
    }
}
