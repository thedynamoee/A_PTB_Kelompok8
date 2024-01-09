package com.eleco.view.dashboard.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.eleco.R;
import com.eleco.model.ButtonItem;
import com.eleco.view.AccountSettingsActivity;
import com.eleco.view.ChangePasswordActivity;
import com.eleco.view.HelpActivity;
import com.eleco.view.TransactionHistoryActivity;
import com.eleco.view.auth.LoginActivity;
import com.eleco.view.dashboard.adapter.ButtonListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfilFragment extends Fragment {

    ListView listView;
    TextView userName, emailTxt;
    ImageView img;
    private DatabaseReference mDatabase;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profil, container, false);

        listView = rootView.findViewById(R.id.listButton);
        userName = rootView.findViewById(R.id.usernametxt);
        emailTxt = rootView.findViewById(R.id.emailtxt);
        img = rootView.findViewById(R.id.userImage);

        final ArrayList<ButtonItem> buttonList = new ArrayList<>();
        buttonList.add(new ButtonItem(R.drawable.ic_setting, "Pengaturan Akun"));
        buttonList.add(new ButtonItem(R.drawable.ic_poin, "Riwayat Transaksi"));
        buttonList.add(new ButtonItem(R.drawable.ic_changepassword, "Ubah Password"));
        buttonList.add(new ButtonItem(R.drawable.ic_bantuan, "Bantuan"));
        buttonList.add(new ButtonItem(R.drawable.ic_logout, "Keluar"));

        ButtonListAdapter adapter = new ButtonListAdapter(requireContext(), R.layout.list_button, buttonList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ButtonItem selectedItem = buttonList.get(position);

                switch (selectedItem.getButtonText()) {
                    case "Pengaturan Akun":
                        Intent accountIntent = new Intent(requireContext(), AccountSettingsActivity.class);
                        startActivity(accountIntent);
                        break;
                    case "Riwayat Transaksi":
                        Intent transactionHistoryIntent = new Intent(requireContext(), TransactionHistoryActivity.class);
                        startActivity(transactionHistoryIntent);
                        break;
                    case "Ubah Password":
                        Intent changePasswordIntent = new Intent(requireContext(), ChangePasswordActivity.class);
                        startActivity(changePasswordIntent);
                        break;
                    case "Bantuan":
                        Intent helpIntent = new Intent(requireContext(), HelpActivity.class);
                        startActivity(helpIntent);
                        break;
                    case "Keluar":
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setMessage("Apakah Anda yakin ingin keluar?")
                                .setCancelable(false)
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        SharedPreferences sharedPreferences1 = requireActivity().getSharedPreferences("my_shared_pref", Context.MODE_PRIVATE);
                                        SharedPreferences sharedPreferences2 = requireActivity().getSharedPreferences("LIKES", Context.MODE_PRIVATE);
                                        SharedPreferences sharedPreferences3 = requireActivity().getSharedPreferences("LIKESCOMMENT", Context.MODE_PRIVATE);

                                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                                        SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                                        SharedPreferences.Editor editor3 = sharedPreferences3.edit();

                                        editor1.clear().apply();
                                        editor2.clear().apply();
                                        editor3.clear().apply();

                                        Intent loginIntent = new Intent(requireContext(), LoginActivity.class);
                                        startActivity(loginIntent);

                                        FirebaseAuth.getInstance().signOut();
                                        requireActivity().finish();
                                    }
                                })
                                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        break;
                    default:
                        break;

                }
            }
        });

        getUserData();

        return rootView;
    }
    private void getUserData(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userID = currentUser.getUid();

            mDatabase.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String username = dataSnapshot.child("name").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);

                        Glide.with(requireContext())
                                .load(imageUrl)
                                .placeholder(R.drawable.ic_user_primary)
                                .into(img);
                        userName.setText(username);
                        emailTxt.setText(email);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
