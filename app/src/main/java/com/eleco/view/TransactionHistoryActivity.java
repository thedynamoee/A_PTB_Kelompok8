package com.eleco.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.eleco.R;
import com.eleco.model.RewardItem;
import com.eleco.model.SetoranData;
import com.eleco.view.dashboard.adapter.RiwayatAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TransactionHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RiwayatAdapter riwayatAdapter;
    private ArrayList<SetoranData> type1DataList;
    private ArrayList<RewardItem> type2DataList;
    private String setoranId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        // Initialize RecyclerView and data lists
        recyclerView = findViewById(R.id.rcRiwayat);
        type1DataList = new ArrayList<>();
        type2DataList = new ArrayList<>();

        riwayatAdapter = new RiwayatAdapter(this, type1DataList, type2DataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(riwayatAdapter);

        getDataSetoran();
        getDataReward();
    }

    private void getDataSetoran() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Setoran").child(userId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                type1DataList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    setoranId = snapshot.getKey();
                    SetoranData setoranData = snapshot.getValue(SetoranData.class);
                    if (setoranData != null) {
                        type1DataList.add(setoranData);
                    }
                }
                riwayatAdapter.notifyDataSetChanged(); // Notify adapter after updating data
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors or onCancelled event
            }
        });
    }

    private void getDataReward() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Rewards").child(userId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                type2DataList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RewardItem rewardItem = snapshot.getValue(RewardItem.class);
                    if (rewardItem != null) {
                        type2DataList.add(rewardItem);
                    }
                }
                riwayatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors or onCancelled event
            }
        });
    }

}
