package com.eleco.view.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.eleco.R;
import com.eleco.model.RewardItem;
import com.eleco.view.MainActivity;
import com.eleco.view.dashboard.adapter.MyRewardAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyRewardActivity extends AppCompatActivity {

    ImageView btBack;
    RecyclerView rcReward;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reward);
        mAuth = FirebaseAuth.getInstance();

        btBack = findViewById(R.id.btBack);
        rcReward = findViewById(R.id.rcReward);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcReward.setLayoutManager(layoutManager);

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyRewardActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        getDataReward();
    }

    private void getDataReward(){
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        DatabaseReference rewardRef = FirebaseDatabase.getInstance().getReference().child("Rewards").child(userId);
        rewardRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<RewardItem> rewardItemList = new ArrayList<>();

                    for (DataSnapshot rewardSnapshot : snapshot.getChildren()) {
                        int imgResource = rewardSnapshot.child("imageResource").getValue(Integer.class);
                        String title = rewardSnapshot.child("title").getValue(String.class);
                        int poin = rewardSnapshot.child("poin").getValue(Integer.class);
                        String setoranid = rewardRef.getKey();
                        RewardItem rewardItem = new RewardItem(setoranid, imgResource, title, poin);
                        rewardItemList.add(rewardItem);
                    }
                    MyRewardAdapter adapter = new MyRewardAdapter(rewardItemList);
                    rcReward.setAdapter(adapter);
                } else {
                    Toast.makeText(MyRewardActivity.this, "Data reward tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}