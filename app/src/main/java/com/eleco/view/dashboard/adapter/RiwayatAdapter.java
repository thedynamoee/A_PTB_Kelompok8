package com.eleco.view.dashboard.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eleco.R;
import com.eleco.model.RewardItem;
import com.eleco.model.SetoranData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RiwayatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM_1 = 1;
    private static final int TYPE_ITEM_2 = 2;


    private ArrayList<SetoranData> type1DataList;
    private ArrayList<RewardItem> type2DataList;
    private Context context;
    public RiwayatAdapter(Context context, ArrayList<SetoranData> type1DataList, ArrayList<RewardItem> type2DataList) {
        this.type1DataList = type1DataList;
        this.type2DataList = type2DataList;
        this.context = context;

    }

    @Override
    public int getItemViewType(int position) {
        if (position < type1DataList.size()) {
            return TYPE_ITEM_1;
        } else {
            return TYPE_ITEM_2;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder;
        View view;

        switch (viewType) {
            case TYPE_ITEM_1:
                view = inflater.inflate(R.layout.item_riwayat_setor, parent, false);
                viewHolder = new Type1ViewHolder(view);
                break;
            case TYPE_ITEM_2:
                view = inflater.inflate(R.layout.item_riwayat_tukar_poin, parent, false);
                viewHolder = new Type2ViewHolder(view);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_ITEM_1) {
            Type1ViewHolder type1ViewHolder = (Type1ViewHolder) holder;
            SetoranData data = type1DataList.get(position);
            type1ViewHolder.title.setText("Setoran : "+data.getNamaLokasi());
            type1ViewHolder.pointxt.setText("+ "+data.getpoin());
            type1ViewHolder.btHapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int adapterPosition = holder.getAdapterPosition();
                    deleteFromFirebase(adapterPosition);
                }
            });
        } else {
            Type2ViewHolder type2ViewHolder = (Type2ViewHolder) holder;
            int adjustedPosition = position - type1DataList.size();
            RewardItem data = type2DataList.get(adjustedPosition);
            type2ViewHolder.poin.setText("- "+data.getPoin());

            type2ViewHolder.btHapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int adapterPosition = holder.getAdapterPosition();
                    deleteFromFirebase(adapterPosition - type1DataList.size());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return type1DataList.size() + type2DataList.size();
    }

    private static class Type1ViewHolder extends RecyclerView.ViewHolder {
        TextView title, pointxt;
        ImageView btHapus;

        Type1ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            pointxt = itemView.findViewById(R.id.poinPlus);
            btHapus = itemView.findViewById(R.id.btHapus);
        }
    }

    private static class Type2ViewHolder extends RecyclerView.ViewHolder {

        TextView poin;
        ImageView btHapus;

        Type2ViewHolder(View itemView) {
            super(itemView);
            poin = itemView.findViewById(R.id.poinMin);
            btHapus = itemView.findViewById(R.id.btHapus);
        }
    }
    private void deleteFromFirebase(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Konfirmasi Hapus");
        builder.setMessage("Apakah Anda yakin ingin menghapus item ini?");

        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String userId = mAuth.getCurrentUser().getUid();
                DatabaseReference databaseReference;

                if (position < type1DataList.size()) {
                    String idSetoran = type1DataList.get(position).getIdSetoran();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Setoran").child(userId).child(idSetoran);
                    databaseReference.removeValue();
                    type1DataList.remove(position);
                } else {
                    int adjustedPosition = position - type1DataList.size();
                    String idReward = type2DataList.get(adjustedPosition).getIdReward();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Rewards").child(userId).child(idReward);
                    databaseReference.removeValue();
                    type2DataList.remove(adjustedPosition);
                }

                // Notify adapter about the item being removed
                notifyItemRemoved(position);
            }
        });

        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do nothing, dismiss dialog
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}

