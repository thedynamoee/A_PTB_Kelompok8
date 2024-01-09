package com.eleco.view.dashboard.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eleco.R;
import com.eleco.model.NotificationItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context mContext;
    private ArrayList<NotificationItem> mNotificationItems;

    public NotificationAdapter(Context context, ArrayList<NotificationItem> notificationItems) {
        this.mContext = context;
        this.mNotificationItems = notificationItems;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNotificationItems.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title;
        ImageView btHapus;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.title);
            btHapus = itemView.findViewById(R.id.btHapus);
        }

        public void bind(int position) {
            NotificationItem item = mNotificationItems.get(position);

            if (item != null) {
                if (item.getTitle().equals("Makasih udah menukarkan poinmu! Nih ada Voucher Diskon untuk kamu!")) {
                    img.setImageResource(R.drawable.bt_tukarpoin);
                } else {
                    img.setImageResource(R.drawable.bt_setor);
                }

                title.setText(item.getTitle());
                btHapus.setOnClickListener(v -> showDeleteConfirmationDialog(item));
            }
        }

        private void showDeleteConfirmationDialog(NotificationItem item) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Hapus Notifikasi");
            builder.setMessage("Apakah Anda yakin ingin menghapus notifikasi ini?");

            builder.setPositiveButton("Ya", (dialog, which) -> {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notifikasi").child(userId);
                    databaseReference.child(item.getNotification_id()).removeValue()
                            .addOnSuccessListener(aVoid -> {
                                int position = getAdapterPosition();
                                if (position != RecyclerView.NO_POSITION) {
                                    mNotificationItems.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, mNotificationItems.size());
                                }
                                Toast.makeText(mContext, "Notifikasi dihapus", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(mContext, "Gagal menghapus notifikasi", Toast.LENGTH_SHORT).show();
                            });
                }
            });

            builder.setNegativeButton("Batal", (dialog, which) -> {
                dialog.dismiss();
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}
