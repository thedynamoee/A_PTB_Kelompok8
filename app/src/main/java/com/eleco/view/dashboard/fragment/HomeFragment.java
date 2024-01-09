package com.eleco.view.dashboard.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eleco.model.ArtikelModel;
import com.eleco.view.dashboard.AllArtikelActivity;
import com.eleco.view.dashboard.NotificationActivity;
import com.eleco.R;
import com.eleco.view.TransactionHistoryActivity;
import com.eleco.view.dashboard.PickUpActivity;
import com.eleco.view.dashboard.adapter.ArtikelAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {


    private TextView usernameTxt, textView2, btSeeAll,txtJumlahPoin;
    private ImageView btNotif;
    private LinearLayout btSetor, btPickUp, btRiwayat, btTukarPoin;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private ArtikelAdapter adapter;
    private ArrayList<ArtikelModel> artikelList;


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        usernameTxt = view.findViewById(R.id.usernametxt);
        txtJumlahPoin = view.findViewById(R.id.txtJumlahPoin);
        textView2 = view.findViewById(R.id.textView2);
        btSeeAll = view.findViewById(R.id.btSeeAll);
        btNotif = view.findViewById(R.id.btNotif);
        btSetor = view.findViewById(R.id.btSetor);
        btPickUp = view.findViewById(R.id.btPickUp);
        btRiwayat = view.findViewById(R.id.btRiwayat);
        btTukarPoin = view.findViewById(R.id.btTukarPoin);

        recyclerView = view.findViewById(R.id.rcArtikel);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 1, GridLayoutManager.HORIZONTAL, false));
        dataArtikel();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userID = currentUser.getUid();

            mDatabase.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String username = dataSnapshot.child("name").getValue(String.class);
                        usernameTxt.setText("Hei, " + username);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            });
        }
        btSetor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new SetorFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        btTukarPoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new PoinFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        btPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), PickUpActivity.class);
                startActivity(intent);
            }
        });
        btRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), TransactionHistoryActivity.class);
                startActivity(intent);
            }
        });
        btNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), NotificationActivity.class);
                startActivity(intent);
            }
        });
        btSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), AllArtikelActivity.class);
                startActivity(intent);
            }
        });
        getPoin();

        return view;
    }
    private void getPoin(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long totalCount = dataSnapshot.child("totalPoin").getValue(Long.class);
                if (totalCount != null) {
                    txtJumlahPoin.setText(totalCount + " EP");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });

    }

    private void dataArtikel(){
        artikelList = new ArrayList<>();
        artikelList.add(new ArtikelModel("Gini nih Elecoers cara ngolah sampah elektronik yang benar", R.drawable.image_artikel1));
        artikelList.add(new ArtikelModel("Baru tau kan kalo sampah elektronik itu berbahaya?", R.drawable.image_artikel2));

        if (artikelList.size() > 5) {
            artikelList.subList(5, artikelList.size()).clear();
        }
        adapter = new ArtikelAdapter(artikelList, requireActivity());
        recyclerView.setAdapter(adapter);

    }



}

