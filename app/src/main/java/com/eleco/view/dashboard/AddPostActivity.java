package com.eleco.view.dashboard;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eleco.R;
import com.eleco.model.PostData;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddPostActivity extends AppCompatActivity {

    private DatabaseReference databaseReference, dbuser;
    private FirebaseAuth mAuth;
    private String username, imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        Spinner spinner = findViewById(R.id.spinner);
        TextInputLayout textInputLayout = findViewById(R.id.textInputLayout);
        Button btPost = findViewById(R.id.btPost);

        ImageView btBack = findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Posts");
        mAuth = FirebaseAuth.getInstance();

        btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedTopic = spinner.getSelectedItem().toString();
                String postText = textInputLayout.getEditText().getText().toString();
                dbuser = FirebaseDatabase.getInstance().getReference().child("users");
                FirebaseUser currentUser = mAuth.getCurrentUser();

                String userId = (currentUser != null) ? currentUser.getUid() : "";
                dbuser.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            PostData postData;
                            username = dataSnapshot.child("name").getValue(String.class);
                            imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                            if  (postText.isEmpty()) {
                                Toast.makeText(AddPostActivity.this, "Postingan tidak boleh kosong", Toast.LENGTH_SHORT).show();
                            } else if (selectedTopic.equals("Pilih Kategori")) {
                                Toast.makeText(AddPostActivity.this, "Pilih kategori terlebih dahulu", Toast.LENGTH_SHORT).show();
                            } else if (postText.length() > 300) {
                                textInputLayout.setError("Maksimal 300 karakter");
                            }
                            String currentDateTime = getCurrentDateTime();
                            DatabaseReference newPostRef = databaseReference.push();
                            String postId = newPostRef.getKey();

                            if (imageUrl == null){
                                postData = new PostData(userId, postId, imageUrl, username, postText,0,0, currentDateTime, selectedTopic);
                            }

                            postData = new PostData(userId, postId, imageUrl, username, postText,0,0, currentDateTime, selectedTopic);
                            newPostRef.setValue(postData);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Penanganan error
                    }
                });
            }
        });
    }
        private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
