package com.eleco.view.dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eleco.R;
import com.eleco.model.ButtonItem;

import java.util.ArrayList;

public class ButtonListAdapter extends ArrayAdapter<ButtonItem> {

    private Context mContext;
    private int mResource;

    public ButtonListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ButtonItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ButtonItem currentItem = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        ImageView imageView = convertView.findViewById(R.id.img);
        TextView textView = convertView.findViewById(R.id.txt);

        imageView.setImageResource(currentItem.getImageId());
        textView.setText(currentItem.getButtonText());


        return convertView;
    }
}
