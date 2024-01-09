package com.eleco.view.dashboard.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eleco.R;
import com.eleco.model.ArtikelModel;

import java.util.ArrayList;

public class ArtikelAdapter extends RecyclerView.Adapter<ArtikelAdapter.ArtikelViewHolder> {

    private ArrayList<ArtikelModel> artikelList;
    private Context context;

    // Constructor menerima ArrayList data dan context
    public ArtikelAdapter(ArrayList<ArtikelModel> artikelList, Context context) {
        this.artikelList = artikelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ArtikelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_artikel, parent, false);
        return new ArtikelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtikelViewHolder holder, int position) {
        ArtikelModel artikel = artikelList.get(position);

        holder.title.setText(artikel.getTitle());
        holder.image.setImageResource(artikel.getImageResource());
    }

    @Override
    public int getItemCount() {
        return artikelList.size();
    }

    public static class ArtikelViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        public ArtikelViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgArtikel);
            title = itemView.findViewById(R.id.titleArtikel);
        }
    }
}
