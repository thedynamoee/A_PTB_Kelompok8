package com.eleco.view.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.eleco.R;
import com.eleco.model.ArtikelModel;
import com.eleco.view.dashboard.adapter.ArtikelAdapter;

import java.util.ArrayList;

public class AllArtikelActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArtikelAdapter adapter;
    private ArrayList<ArtikelModel> artikelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_artikel);

        recyclerView = findViewById(R.id.rcAllArtikel);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        artikelList = new ArrayList<>();
        artikelList.add(new ArtikelModel("Gini nih Elecoers cara ngolah sampah elektronik yang benar", R.drawable.image_artikel1));
        artikelList.add(new ArtikelModel("Baru tau kan kalo sampah elektronik itu berbahaya?", R.drawable.image_artikel2));

        adapter = new ArtikelAdapter(artikelList, this);
        recyclerView.setAdapter(adapter);
    }
}