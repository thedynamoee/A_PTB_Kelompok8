package com.eleco.view.dashboard.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.eleco.R;
import com.eleco.model.PostData;
import com.eleco.view.dashboard.CommentActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.List;

public class PostAdapter extends ArrayAdapter<PostData> {

    private Context mContext;
    private List<PostData> mData;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    public PostAdapter(Context context, List<PostData> data) {
        super(context, 0, data);
        mContext = context;
        mData = data;
        sharedPreferences = context.getSharedPreferences("LIKES", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    static class ViewHolder {
        ImageView imgUser, btLike, btComment, btShare, btHapus;
        TextView txtName, txtPost, txtCountLike, txtCountComment;
        boolean isLiked;

        ViewHolder(View convertView) {
            imgUser = convertView.findViewById(R.id.imgUser);
            btLike = convertView.findViewById(R.id.btLike);
            btComment = convertView.findViewById(R.id.btComment);
            btShare = convertView.findViewById(R.id.btShare);
            txtName = convertView.findViewById(R.id.txtName);
            txtPost = convertView.findViewById(R.id.txtPost);
            txtCountLike = convertView.findViewById(R.id.txtCountLike);
            txtCountComment = convertView.findViewById(R.id.txtCountComment);
            btHapus = convertView.findViewById(R.id.btHapus);

        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;


        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_postingan, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PostData currentPost = mData.get(position);


        Glide.with(mContext)
                .load(currentPost.getimageUrl())
                .placeholder(R.drawable.ic_user)
                .into(holder.imgUser);
        holder.txtName.setText(currentPost.getUserName());
        holder.txtPost.setText(currentPost.getPostText());

        if (currentPost.getLikeCount() > 0){
            holder.txtCountLike.setText(String.valueOf(currentPost.getLikeCount()));

        }else {
            holder.txtCountLike.setText("");
        }

        if (currentPost.getCommentCount() > 0){
            holder.txtCountComment.setText(String.valueOf(currentPost.getCommentCount()));
        }else {
            holder.txtCountComment.setText("");
        }



        boolean isLiked = sharedPreferences.getBoolean(currentPost.getIdPost(), false);
        if (isLiked) {
            holder.btLike.setImageResource(R.drawable.ic_like_fill);
        } else {
            holder.btLike.setImageResource(R.drawable.ic_like);
        }
        holder.isLiked = isLiked;

        holder.btLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int likeCount;
                DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(currentPost.getIdPost());
                boolean isLiked = sharedPreferences.getBoolean(currentPost.getIdPost(), false);
                if (!isLiked) {
                    holder.isLiked = true;
                    holder.btLike.setImageResource(R.drawable.ic_like_fill);
                    likeCount = currentPost.getLikeCount() + 1;
                    editor.putBoolean(currentPost.getIdPost(), true);
                } else {
                    holder.isLiked = false;
                    holder.btLike.setImageResource(R.drawable.ic_like);
                    likeCount = currentPost.getLikeCount() - 1;
                    editor.putBoolean(currentPost.getIdPost(), false);
                }
                editor.apply();
                postRef.child("likeCount").setValue(likeCount);
                holder.isLiked = !isLiked;

            }
        });


        holder.btComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postId", currentPost.getIdPost());
                mContext.startActivity(intent);
            }
        });

        holder.btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareBody = "Judul Postingan: " + currentPost.getPostText(); // Sesuaikan dengan konten postingan yang ingin Anda bagikan
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Judul Postingan");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                mContext.startActivity(Intent.createChooser(shareIntent, "Bagikan postingan melalui"));
            }
        });

        holder.btHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null && currentPost.getIdUser().equals(currentUser.getUid())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage("Hapus\n\nAnda yakin ingin menghapus postingan ini?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mData.remove(position);
                                    notifyDataSetChanged();

                                    DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(currentPost.getIdPost());
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


}

