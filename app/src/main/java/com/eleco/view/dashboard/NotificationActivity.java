package com.eleco.view.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;

import com.eleco.R;
import com.eleco.model.NotificationItem;
import com.eleco.view.dashboard.adapter.NotificationAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView rcNotif;
    private ImageView btBack;
    private ArrayList<NotificationItem> notificationList;
    private NotificationAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        rcNotif = findViewById(R.id.rcNotifikasi);
        btBack = findViewById(R.id.btBack);

        rcNotif.setLayoutManager(new LinearLayoutManager(this));
        notificationList = new ArrayList<>();
        adapter = new NotificationAdapter(this, notificationList);
        rcNotif.setAdapter(adapter);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notifikasi").child(userId);

            databaseReference.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    notificationList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String id = snapshot.child("id").getValue(String.class);
                        String teksNotifikasi = snapshot.child("teksNotifikasi").getValue(String.class);
                        Long timestamp = snapshot.child("timestamp").getValue(Long.class);

                        notificationList.add(new NotificationItem(id, teksNotifikasi, timestamp));
                    }
                    Collections.reverse(notificationList);

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Penanganan kesalahan jika ada
                }
            });
        }
    }
}