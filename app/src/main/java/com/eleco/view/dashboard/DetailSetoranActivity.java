package com.eleco.view.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eleco.R;
import com.eleco.model.SetoranData;
import com.eleco.view.MainActivity;
import com.eleco.view.dashboard.adapter.ItemListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class DetailSetoranActivity extends AppCompatActivity {

    ImageView btBack;
    TextView txtNameLokasi, txtDescLokasi, txtTotalPoin;
    ListView listItem;
    AppCompatButton btSetor;

    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_setoran);


        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Setoran");


        btBack = findViewById(R.id.btBack);
        txtNameLokasi = findViewById(R.id.txtNameLokasi);
        txtDescLokasi = findViewById(R.id.txtDescLokasi);
        listItem = findViewById(R.id.list_item);
        txtTotalPoin = findViewById(R.id.txtTotalPoin);
        btSetor = findViewById(R.id.btSetor);

        Intent intent = getIntent();
        ArrayList<String> jenisItems = new ArrayList<>();
        ArrayList<Integer> jumlahItems = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            String jenisItem = intent.getStringExtra("jenis_item" + i);
            int jumlahItem = intent.getIntExtra("jumlah_item" + i, 0);

            if (jenisItem != null && !jenisItem.isEmpty() && jumlahItem > 0) {
                jenisItems.add(jenisItem);
                jumlahItems.add(jumlahItem);
            }
        }

        ItemListAdapter adapter = new ItemListAdapter(this, jenisItems, jumlahItems);
        listItem.setAdapter(adapter);

        String namaLokasi = intent.getStringExtra("nama_lokasi");
        String deskripsiLokasi = intent.getStringExtra("deskripsi_lokasi");

        if (namaLokasi != null && deskripsiLokasi != null) {
            txtNameLokasi.setText(namaLokasi);
            txtDescLokasi.setText(deskripsiLokasi);
        }

        int totalPoint = intent.getIntExtra("totalCount", 0);
        txtTotalPoin.setText(totalPoint + " EP");
        addPointsToUser(totalPoint);

        btSetor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namaLokasi = txtNameLokasi.getText().toString();
                String deskripsiLokasi = txtDescLokasi.getText().toString();

                String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                DatabaseReference userRef = databaseReference.child(userId);
                String setoranId = userRef.push().getKey();

                SetoranData setoranData = new SetoranData(setoranId, namaLokasi, deskripsiLokasi, totalPoint, jenisItems, jumlahItems);
                assert setoranId != null;
                userRef.child(setoranId).setValue(setoranData);
                String notifTxt = "Yeay! Kamu dapat " + totalPoint + " EP dari setoran E-Waste kamu kali ini, Elecoers";

                Intent intent = new Intent(DetailSetoranActivity.this, SetorFinishActivity.class);
                intent.putExtra("totalPoin", totalPoint);
                displaySuccessNotification(notifTxt);
                startActivity(intent);
                finish();
                Toast.makeText(DetailSetoranActivity.this, "Setor E - Waste Berhasil", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void addPointsToUser(int totalPoint) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int totalPoinUser = 0;

                    if (dataSnapshot.child("totalPoin").getValue() != null) {
                        totalPoinUser = dataSnapshot.child("totalPoin").getValue(Integer.class);
                    }

                    totalPoinUser += totalPoint;

                    usersRef.child("totalPoin").setValue(totalPoinUser)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {

                                }
                            })
                            .addOnFailureListener(e -> {

                                Toast.makeText(DetailSetoranActivity.this, "Gagal menyimpan poin: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                } else {
                    // Handle the case where user data doesn't exist
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
    private void displaySuccessNotification(String notifTxt) {
        int idNotif = (int) System.currentTimeMillis();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Sukses!")
                .setContentText(notifTxt)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(idNotif, builder.build());
    }



}
