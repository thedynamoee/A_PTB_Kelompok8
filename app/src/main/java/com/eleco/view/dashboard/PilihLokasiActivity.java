package com.eleco.view.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.eleco.R;
import com.eleco.model.ModelLokasi;
import com.eleco.view.dashboard.adapter.LokasiAdapter;

import java.util.ArrayList;
import java.util.List;

public class PilihLokasiActivity extends AppCompatActivity implements LokasiAdapter.OnItemClickListener {

    private RecyclerView rcLokasi;
    private LokasiAdapter lokasiAdapter;
    private EditText search;
    private ImageView btBack;
    private List<ModelLokasi> originalLokasiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_lokasi);

        rcLokasi = findViewById(R.id.rcLokasi);
        search = findViewById(R.id.search);
        btBack = findViewById(R.id.btBack);
        rcLokasi.setLayoutManager(new LinearLayoutManager(this));

        originalLokasiList = generateSampleData();

        List<ModelLokasi> lokasiList = new ArrayList<>(originalLokasiList);
        lokasiAdapter = new LokasiAdapter(lokasiList, this);
        rcLokasi.setAdapter(lokasiAdapter);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterLokasi(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private List<ModelLokasi> generateSampleData() {
        List<ModelLokasi> lokasiList = new ArrayList<>();
        lokasiList.add(new ModelLokasi("Nama Lokasi 1", "Deskripsi Lokasi 1"));
        lokasiList.add(new ModelLokasi("Nama Lokasi 2", "Deskripsi Lokasi 2"));
        return lokasiList;
    }

    @Override
    public void onItemClick(ModelLokasi modelLokasi) {
        String namaLokasi = modelLokasi.getNama();
        String deskripsiLokasi = modelLokasi.getDeskripsi();

        Log.d("data lokasi", namaLokasi + deskripsiLokasi);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("nama_lokasi", namaLokasi);
        resultIntent.putExtra("deskripsi_lokasi", deskripsiLokasi);
        setResult(RESULT_OK, resultIntent);

        finish();
    }
    private void filterLokasi(String searchText) {
        List<ModelLokasi> filteredList = new ArrayList<>();

        for (ModelLokasi lokasi : originalLokasiList) {
            if (lokasi.getNama().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(lokasi);
            }
        }

        lokasiAdapter.filterList(filteredList);
    }
}
