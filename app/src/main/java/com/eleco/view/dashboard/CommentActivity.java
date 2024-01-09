package com.eleco.view.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.eleco.R;
import com.eleco.model.CommentData;
import com.eleco.view.dashboard.adapter.CommentAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {

    private TextView textView5, txtName, txtPost, txtCountLike, txtCountComment;
    private ImageView btBack, imgUser, btLike, btComment, btShare, btSendComment;

    private ListView listPost;
    private TextInputLayout textInputLayout;
    private TextInputEditText textInputPost;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FirebaseAuth mAuth;
    private ArrayList<CommentData> commentList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        mAuth = FirebaseAuth.getInstance();


        btBack = findViewById(R.id.btBack);
        textView5 = findViewById(R.id.textView5);
        imgUser = findViewById(R.id.imgUser);
        txtName = findViewById(R.id.txtName);
        txtPost = findViewById(R.id.txtPost);
        btLike = findViewById(R.id.btLike);

        txtCountLike = findViewById(R.id.txtCountLike);
        txtCountComment = findViewById(R.id.txtCountComment);
        btShare = findViewById(R.id.btShare);
        listPost = findViewById(R.id.listPost);
        textInputLayout = findViewById(R.id.textInputLayout);
        textInputPost = findViewById(R.id.textInputPost);
        btSendComment = findViewById(R.id.btSendComment);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        String postId = intent.getStringExtra("postId");
        countComments(postId);
        getPostingan(postId);
        getComment(postId);

        sharedPreferences = this.getSharedPreferences("LIKES", Context.MODE_PRIVATE);

        boolean isLiked = sharedPreferences.getBoolean(postId, false);
        if (isLiked) {
            btLike.setImageResource(R.drawable.ic_like_fill);
        } else {
            btLike.setImageResource(R.drawable.ic_like);
        }

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference commentsReference = firebaseDatabase.getReference().child("Comments");

        btSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentText = textInputPost.getText().toString().trim();
                DatabaseReference dbuser = FirebaseDatabase.getInstance().getReference().child("users");
                FirebaseUser currentUser = mAuth.getCurrentUser();
                String userId = (currentUser != null) ? currentUser.getUid() : "";
                dbuser.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String username = dataSnapshot.child("name").getValue(String.class);
                            String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                            if (commentText.length() > 300) {
                                textInputLayout.setError("Maksimal 300 karakter");

                            }else {
                                DatabaseReference newPostRef = commentsReference.push();
                                String commentId = newPostRef.getKey();
                                if (!commentText.isEmpty()) {
                                    CommentData newComment = new CommentData(
                                            userId,
                                            postId,
                                            commentId,
                                            imageUrl,
                                            username,
                                            commentText,
                                            0
                                    );

                                    newPostRef.setValue(newComment);
                                    textInputPost.setText("");
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }

    private void getPostingan(String postId){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Posts").child(postId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String name = dataSnapshot.child("userName").getValue(String.class);
                    String postContent = dataSnapshot.child("postText").getValue(String.class);
                    String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                    Long likeCount = dataSnapshot.child("likeCount").getValue(Long.class);
                    Long commentCount = dataSnapshot.child("commentCount").getValue(Long.class);
                    txtCountLike.setText(String.valueOf(likeCount));
                    txtName.setText(name);
                    txtPost.setText(postContent);
                    txtCountComment.setText(String.valueOf(commentCount));
                    Glide.with(CommentActivity.this)
                            .load(imageUrl)
                            .placeholder(R.drawable.ic_user)
                            .into(imgUser);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error here
            }
        });
    }
    private void getComment(String postId){
        DatabaseReference commentsReference = FirebaseDatabase.getInstance().getReference().child("Comments");
        commentsReference.orderByChild("idPost").equalTo(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        CommentData comment = snapshot.getValue(CommentData.class);
                        commentList.add(comment);
                    }
                }


                CommentAdapter adapter = new CommentAdapter(CommentActivity.this, commentList);
                listPost.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }
    private void countComments(String postId){
        DatabaseReference commentsReference = FirebaseDatabase.getInstance().getReference().child("Comments");
        commentsReference.orderByChild("idPost").equalTo(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int commentCount = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    commentCount++;
                    DatabaseReference postsReference = FirebaseDatabase.getInstance().getReference().child("Posts").child(postId);

                    postsReference.child("commentCount").setValue(commentCount)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Comment Count", "Comment count updated successfully!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("Comment Count", "Failed to update comment count: " + e.getMessage());
                                }
                            });
                }

                Log.d("Comment Count", "Total Comments: " + commentCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }

}