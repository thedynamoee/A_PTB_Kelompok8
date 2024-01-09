package com.eleco.view.dashboard.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.eleco.R;
import com.eleco.view.dashboard.AddPostActivity;
import com.eleco.view.dashboard.adapter.PostAdapter;
import com.eleco.model.PostData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Collections;

public class KomunitasFragment extends Fragment {

    private ListView listPost;
    private DatabaseReference databaseReference;
    private FloatingActionButton btNewPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_komunitas, container, false);
        listPost = view.findViewById(R.id.listPost);
        btNewPost = view.findViewById(R.id.btNewPost);

        btNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), AddPostActivity.class);
                startActivity(intent);
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Posts");
        loadPosts("newest");
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        if (tabLayout != null) {
            TabLayout.Tab tabNewest = tabLayout.newTab().setText("Newest");
            tabLayout.addTab(tabNewest);

            TabLayout.Tab tabPopular = tabLayout.newTab().setText("Popular");
            tabLayout.addTab(tabPopular);

            TabLayout.Tab tabMostLiked = tabLayout.newTab().setText("Most Liked");
            tabLayout.addTab(tabMostLiked);

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch (tab.getPosition()) {
                        case 0:
                            // Filter: Paling Baru
                            loadPosts("newest");
                            break;
                        case 1:
                            // Filter: Populer
                            loadPosts("popular");
                            break;
                        case 2:
                            // Filter: Paling Disukai
                            loadPosts("most_liked");
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    // Aksi yang dilakukan ketika tab tidak dipilih lagi
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    // Aksi yang dilakukan ketika tab yang sudah dipilih kembali dipilih
                }
            });
        }

        return view;
    }


    private void loadPosts(String filter) {
        Query query = databaseReference.orderByChild("createdAt");
        if (filter.equals("newest")) {
            query = databaseReference.orderByChild("createdAt");
        } else if (filter.equals("popular")) {
            query = databaseReference.orderByChild("commentCount");
        } else if (filter.equals("most_liked")) {
            query = databaseReference.orderByChild("likeCount");
        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<PostData> posts = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PostData post = snapshot.getValue(PostData.class);
                    posts.add(post);
                }
                Collections.reverse(posts);
                displayPosts(posts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }

    private void displayPosts(ArrayList<PostData> posts) {
        PostAdapter postAdapter = new PostAdapter(getContext(), posts);
        listPost.setAdapter(postAdapter);
    }




}
