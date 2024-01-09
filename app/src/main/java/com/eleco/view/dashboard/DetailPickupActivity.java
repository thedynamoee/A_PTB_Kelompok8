package com.eleco.view.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eleco.R;
import com.eleco.model.SetoranData;
import com.eleco.view.dashboard.adapter.ItemListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class DetailPickupActivity extends AppCompatActivity {

    ImageView btBack;
    TextView txtNameLokasi, txtDescLokasi, txtTotalPoin;
    ListView listItem;
    AppCompatButton btSetor;

    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pickup);


        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("PickUp");


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

        if (namaLokasi != null) {
            txtNameLokasi.setText(namaLokasi);
        }

        int totalPoint = intent.getIntExtra("totalCount", 0);
        txtTotalPoin.setText(totalPoint + " EP");
        addPointsToUser(totalPoint);

        btSetor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namaLokasi = txtNameLokasi.getText().toString();

                String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                DatabaseReference userRef = databaseReference.child(userId);
                String setoranId = userRef.push().getKey();

                SetoranData setoranData = new SetoranData(setoranId, namaLokasi, "", totalPoint, jenisItems, jumlahItems);
                assert setoranId != null;
                userRef.child(setoranId).setValue(setoranData);


                Intent intent = new Intent(DetailPickupActivity.this, PickupFinishActivity.class);
                intent.putExtra("nama_lokasi", namaLokasi);
                intent.putExtra("jenisItem", jenisItems);
                intent.putExtra("jumlahItem", jumlahItems);
                startActivity(intent);
                finish();

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

                                Toast.makeText(DetailPickupActivity.this, "Gagal menyimpan poin: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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



}
