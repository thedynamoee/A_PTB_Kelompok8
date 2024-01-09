package com.eleco.view.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eleco.R;
import com.eleco.view.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SetorFinishActivity extends AppCompatActivity {

    private ImageView btBack;
    private TextView textPoin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setor_finish);

        btBack = findViewById(R.id.btBack);
        textPoin = findViewById(R.id.textPoin);

        Intent intent = getIntent();

        int txtPoin = intent.getIntExtra("totalPoin", 0);
        textPoin.setText("Yeay! Kamu dapat " + txtPoin + " EP");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notifikasi").child(userId);
        String notifTxt = "Yeay! Kamu dapat " + txtPoin + " EP dari setoran E-Waste kamu kali ini, Elecoers";
        Long timeStamp = System.currentTimeMillis();

        String idNotif = databaseReference.push().getKey();
        Map<String, Object> notifMap = new HashMap<>();

        notifMap.put("id",idNotif);
        notifMap.put("teksNotifikasi", notifTxt);
        notifMap.put("timestamp", timeStamp);

        if (idNotif != null) {
            databaseReference.child(idNotif).setValue(notifMap);
        }


        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetorFinishActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SetorFinishActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}