package com.eleco.view.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.eleco.R;

import java.util.ArrayList;

public class PickupFinishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_finish);

        ImageView btBack = findViewById(R.id.btBack);
        AppCompatButton btnextChat = findViewById(R.id.btnextChat);

        Intent intent = getIntent();

        String namaLokasi = intent.getStringExtra("nama_lokasi");
        ArrayList<String> jenisItems = intent.getStringArrayListExtra("jenisItem");
        ArrayList<Integer> jumlahItems = intent.getIntegerArrayListExtra("jumlahItem");

        String pesan = "Halo! Saya ingin melakukan setoran e-waste di " + namaLokasi +
                ". Jenis E - Waste: " + jenisItems +
                ". Jumlah Item: " + jumlahItems;
        String nomorTelepon = "+6282173926690";

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnextChat.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("QueryPermissionsNeeded")
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + nomorTelepon + "&text=" + pesan);

                Intent whatsappIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(whatsappIntent);
            }
        });

    }
}