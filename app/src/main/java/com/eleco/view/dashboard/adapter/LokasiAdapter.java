package com.eleco.view.dashboard.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.eleco.R;
import com.eleco.model.ModelLokasi;

import java.util.ArrayList;
import java.util.List;

public class LokasiAdapter extends RecyclerView.Adapter<LokasiAdapter.LokasiViewHolder> {

    private List<ModelLokasi> lokasiList;
    private List<ModelLokasi> lokasiListFull;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ModelLokasi modelLokasi);
    }


    public LokasiAdapter(List<ModelLokasi> lokasiList, OnItemClickListener listener) {
        this.lokasiList = lokasiList;
        this.lokasiListFull = new ArrayList<>(lokasiList);
        this.listener = listener;
    }

    public static class LokasiViewHolder extends RecyclerView.ViewHolder {
        TextView txtNamaLokasi;
        TextView txtDeskripsiLokasi;

        public LokasiViewHolder(View itemView) {
            super(itemView);
            txtNamaLokasi = itemView.findViewById(R.id.txtNameLokasi);
            txtDeskripsiLokasi = itemView.findViewById(R.id.txtDescLokasi);
        }

        public void bind(final ModelLokasi modelLokasi, final OnItemClickListener listener) {
            txtNamaLokasi.setText(modelLokasi.getNama());
            txtDeskripsiLokasi.setText(modelLokasi.getDeskripsi());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(modelLokasi);
                }
            });
        }
    }

    @NonNull
    @Override
    public LokasiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lokasi, parent, false);
        return new LokasiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LokasiViewHolder holder, int position) {
        ModelLokasi modelLokasi = lokasiList.get(position);
        holder.bind(modelLokasi, listener);
    }

    @Override
    public int getItemCount() {
        return lokasiList.size();
    }

    public void filterList(List<ModelLokasi> filteredList) {
        lokasiList = filteredList;
        notifyDataSetChanged();
    }

    public void resetList() {
        lokasiList.clear();
        lokasiList.addAll(lokasiListFull);
        notifyDataSetChanged();
    }
}

