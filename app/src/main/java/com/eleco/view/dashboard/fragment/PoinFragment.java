package com.eleco.view.dashboard.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eleco.R;
import com.eleco.model.RewardItem;
import com.eleco.view.dashboard.MyRewardActivity;
import com.eleco.view.dashboard.adapter.RewardAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PoinFragment extends Fragment {

    TextView txtPoin, btReward;
    EditText search;
    RecyclerView rcReward;

    RewardAdapter adapter;
    private List<RewardItem> dummyDataList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poin, container, false);

        txtPoin = view.findViewById(R.id.txtpoin);
        btReward = view.findViewById(R.id.btReward);
        search = view.findViewById(R.id.search);
        rcReward = view.findViewById(R.id.rcReward);
        rcReward.setLayoutManager(new LinearLayoutManager(requireContext()));

        dummyDataList = createDummyData();

        List<RewardItem> datareward = new ArrayList<>(dummyDataList);

        adapter = new RewardAdapter(datareward);
        rcReward.setAdapter(adapter);
        btReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), MyRewardActivity.class);
                startActivity(intent);
            }
        });

        adapter.setRewardItemClickListener(new RewardAdapter.RewardItemClickListener() {
            @Override
            public void onRewardItemClick(RewardItem rewardItem) {
                showCustomDialog(rewardItem);
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });
        getUsersPoin();

        return view;
    }


    private void getUsersPoin(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long totalPoin = snapshot.child("totalPoin").getValue(Long.class);
                txtPoin.setText(String.valueOf(totalPoin)+" EP");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public List<RewardItem> createDummyData() {
        List<RewardItem> datareward = new ArrayList<>();
        DatabaseReference rewardsRef = FirebaseDatabase.getInstance().getReference().child("Rewards");
        String setoranId = rewardsRef.push().getKey();
        datareward.add(new RewardItem(setoranId,R.drawable.image_reward1, "Bebas akses 7 Hari Viu Premium", 10));
        datareward.add(new RewardItem(setoranId,R.drawable.image_reward2, "Voucher Zalora DISKON 10% All Coustemer", 20));

        return datareward;
    }


    private void filter(String searchText) {
        List<RewardItem> filteredList = new ArrayList<>();

        for (RewardItem rewardItem : dummyDataList) {
            if (rewardItem.getTitle().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(rewardItem);
            }
        }

        adapter.filterList(filteredList);
    }
    private void showCustomDialog(RewardItem rewardItem) {
        Dialog dialog = new Dialog(requireActivity());
        dialog.setContentView(R.layout.reward_dialog_layout);

        TextView textView = dialog.findViewById(R.id.textView);
        textView.setText("Kamu akan menukarkan " + rewardItem.getPoin() + " EP dengan " + rewardItem.getTitle());

        AppCompatButton btBatal = dialog.findViewById(R.id.btBatal);
        AppCompatButton btConfirm = dialog.findViewById(R.id.btConfirm);
        String notifTxt = "Makasih udah menukarkan poinmu! Nih ada Voucher Diskon untuk kamu!";


        btBatal.setOnClickListener(v -> dialog.dismiss());
        btConfirm.setOnClickListener(v -> {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
            int poinToRedeem = rewardItem.getPoin();
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int totalCount = dataSnapshot.child("totalPoin").getValue(Integer.class);
                    if (totalCount >= poinToRedeem) {
                        int updatePoin = totalCount - poinToRedeem;

                        usersRef.child("totalPoin").setValue(updatePoin)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        dialog.dismiss();
                                        showSuccessDialog();
                                        displaySuccessNotification(notifTxt);
                                        saveRewardToFirebase(rewardItem);
                                        saveNotif();
                                    } else {
                                        Toast.makeText(requireActivity(), "Gagal menyimpan poin", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(requireActivity(), "Gagal menyimpan poin: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(requireActivity(), "Poin tidak mencukupi untuk ditukarkan", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                }
            });


            dialog.dismiss();
        });

        dialog.show();
    }
    private void showSuccessDialog() {
        Dialog successDialog = new Dialog(requireActivity());
        successDialog.setContentView(R.layout.confirm_dialog_custom);

        AppCompatButton btConfirm = successDialog.findViewById(R.id.btConfirm);

        btConfirm.setOnClickListener(v -> {
            successDialog.dismiss();
        });

        successDialog.show();
    }
    private void saveRewardToFirebase(RewardItem newReward) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference rewardsRef = FirebaseDatabase.getInstance().getReference().child("Rewards").child(userId);
        String setoranId = rewardsRef.push().getKey();
        rewardsRef.child(setoranId).setValue(newReward)
                .addOnSuccessListener(aVoid -> {

                    Toast.makeText(requireActivity(), "Data reward berhasil disimpan!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(requireActivity(), "Gagal menyimpan data reward: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    private void displaySuccessNotification(String notifTxt) {
        int idNotif = (int) System.currentTimeMillis();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireActivity(), "channel_id")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Sukses!")
                .setContentText(notifTxt)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireActivity());
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
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
    private void saveNotif(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notifikasi").child(userId);
        String notifTxt = "Makasih udah menukarkan poinmu! Nih ada Voucher Diskon untuk kamu!";
        Long timeStamp = System.currentTimeMillis();
        String idNotif = databaseReference.push().getKey();
        Map<String, Object> notifMap = new HashMap<>();

        notifMap.put("id",idNotif);
        notifMap.put("teksNotifikasi", notifTxt);
        notifMap.put("timestamp", timeStamp);



        if (idNotif != null) {
            databaseReference.child(idNotif).setValue(notifMap);
        }


    }




}
