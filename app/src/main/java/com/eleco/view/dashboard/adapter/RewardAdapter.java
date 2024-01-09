package com.eleco.view.dashboard.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eleco.R;
import com.eleco.model.RewardItem;

import java.util.ArrayList;
import java.util.List;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.RewardViewHolder> {

    private List<RewardItem> rewardList;
    private List<RewardItem> rewardItemList;
    private RewardItemClickListener rewardItemClickListener;

    public RewardAdapter(List<RewardItem> rewardList) {
        this.rewardList = rewardList;
        this.rewardItemList = new ArrayList<>(rewardList);
    }
    public interface RewardItemClickListener {
        void onRewardItemClick(RewardItem rewardItem);
    }

    public void setRewardItemClickListener(RewardItemClickListener listener) {
        this.rewardItemClickListener = listener;
    }


    public static class RewardViewHolder extends RecyclerView.ViewHolder {
        ImageView imgReward;
        TextView title, poin;

        public RewardViewHolder(View itemView) {
            super(itemView);
            imgReward = itemView.findViewById(R.id.imgReward);
            title = itemView.findViewById(R.id.title);
            poin = itemView.findViewById(R.id.poin);
        }

        public void bind(final RewardItem rewardItem) {
            imgReward.setImageResource(rewardItem.getImageResource());
            title.setText(rewardItem.getTitle());
            poin.setText(String.valueOf(rewardItem.getPoin()) + " EP");

        }
    }

    @NonNull
    @Override
    public RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reward, parent, false);
        return new RewardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardViewHolder holder, int position) {
        RewardItem rewardItem = rewardList.get(position);
        holder.bind(rewardItem);
        holder.itemView.setOnClickListener(v -> {
            if (rewardItemClickListener != null) {
                rewardItemClickListener.onRewardItemClick(rewardItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rewardList.size();
    }

    public void filterList(List<RewardItem> filteredList) {
        rewardList = filteredList;
        notifyDataSetChanged();
    }

}
