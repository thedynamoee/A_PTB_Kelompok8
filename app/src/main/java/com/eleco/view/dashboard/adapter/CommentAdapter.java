package com.eleco.view.dashboard.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.eleco.R;
import com.eleco.model.CommentData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CommentData> commentList;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    public CommentAdapter(Context context, ArrayList<CommentData> commentList) {
        this.context = context;
        this.commentList = commentList;
        sharedPreferences = context.getSharedPreferences("LIKESCOMMENT", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
            holder = new ViewHolder();
            holder.imgUser = convertView.findViewById(R.id.imgUser);
            holder.txtName = convertView.findViewById(R.id.txtName);
            holder.txtPost = convertView.findViewById(R.id.txtPost);
            holder.btLike = convertView.findViewById(R.id.btLike);
            holder.txtCountLike = convertView.findViewById(R.id.txtCountLike);
            holder.btHapus = convertView.findViewById(R.id.btHapus);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CommentData comment = commentList.get(position);

        holder.txtName.setText(comment.getUserName());
        holder.txtPost.setText(comment.getPostText());
        if (comment.getLikeCount() > 0){
            holder.txtCountLike.setText(String.valueOf(comment.getLikeCount()));

        }else {
            holder.txtCountLike.setText("");
        }

        Glide.with(context).load(comment.getimageUrl()).placeholder(R.drawable.ic_user).into(holder.imgUser);
        boolean isLiked = sharedPreferences.getBoolean(comment.getIdComment(), false);
        if (isLiked) {
            holder.btLike.setImageResource(R.drawable.ic_like_fill);
        } else {
            holder.btLike.setImageResource(R.drawable.ic_like);
        }

        // Set status liked pada holder
        holder.isLiked = isLiked;

        holder.btLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int likeCount;
                DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("Comments").child(comment.getIdComment());
                boolean isLiked = sharedPreferences.getBoolean(comment.getIdComment(), false);
                if (!isLiked) {
                    holder.isLiked = true;
                    holder.btLike.setImageResource(R.drawable.ic_like_fill);
                    likeCount = comment.getLikeCount() + 1;
                    editor.putBoolean(comment.getIdComment(), true);
                } else {
                    holder.isLiked = false;
                    holder.btLike.setImageResource(R.drawable.ic_like);
                    likeCount = comment.getLikeCount() - 1;
                    editor.putBoolean(comment.getIdComment(), false);
                }
                editor.apply();
                postRef.child("likeCount").setValue(likeCount);
                holder.isLiked = !isLiked;

            }
        });
        holder.btHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null && comment.getIdUser().equals(currentUser.getUid())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Hapus\n\nAnda yakin ingin menghapus komentar ini?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    commentList.remove(position);
                                    notifyDataSetChanged();

                                    DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("Comments").child(comment.getIdComment());
                                    DatabaseReference postsReference = FirebaseDatabase.getInstance().getReference().child("Posts").child(comment.getIdPost());
                                    postsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                int currentCommentCount = dataSnapshot.child("commentCount").getValue(Integer.class);
                                                // Mengurangi commentCount
                                                currentCommentCount--;

                                                // Update nilai commentCount yang baru
                                                postsReference.child("commentCount").setValue(currentCommentCount);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            // Penanganan kesalahan jika ada
                                        }
                                    });
                                    postRef.removeValue();
                                }
                            })
                            .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Log.d("ads", "ads");
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView imgUser, btLike, btHapus;
        TextView txtName, txtPost, txtCountLike;
        boolean isLiked;

    }
}

