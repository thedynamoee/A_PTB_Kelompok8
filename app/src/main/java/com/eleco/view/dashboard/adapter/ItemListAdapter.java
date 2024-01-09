package com.eleco.view.dashboard.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eleco.R;

import java.util.ArrayList;

public class ItemListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final ArrayList<String> jenisItems;
    private final ArrayList<Integer> jumlahItems;

    public ItemListAdapter(Context context, ArrayList<String> jenisItems, ArrayList<Integer> jumlahItems) {
        super(context, R.layout.list_itemdetail, jenisItems);
        this.context = context;
        this.jenisItems = jenisItems;
        this.jumlahItems = jumlahItems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder")
        View rowView = inflater.inflate(R.layout.list_itemdetail, parent, false);

        TextView txtJenis = rowView.findViewById(R.id.txtJenis);
        TextView txtCount = rowView.findViewById(R.id.txtCount);

        txtJenis.setText(jenisItems.get(position));
        txtCount.setText(String.valueOf(jumlahItems.get(position)));

        return rowView;
    }
}
