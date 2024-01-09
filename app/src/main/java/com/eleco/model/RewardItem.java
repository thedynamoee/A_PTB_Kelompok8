package com.eleco.model;

public class RewardItem {
    private String idReward;
    private int imageResource;
    private String title;
    private int poin;
    private long timestamp;

    public RewardItem() {
        // Konstruktor tanpa argumen diperlukan untuk Firebase Database
    }



    public RewardItem(String idReward, int imageResource, String title, int poin) {
        this.idReward = idReward;
        this.imageResource = imageResource;
        this.title = title;
        this.poin = poin;
        this.timestamp = System.currentTimeMillis();
    }
    public String getIdReward() {
        return idReward;
    }

    public void setIdReward(String idReward) {
        this.idReward = idReward;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getTitle() {
        return title;
    }

    public int getPoin() {
        return poin;
    }
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
