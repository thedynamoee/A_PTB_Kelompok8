package com.eleco.model;

public class ButtonItem {
    private int imageId;
    private String buttonText;

    public ButtonItem(int imageId, String buttonText) {
        this.imageId = imageId;
        this.buttonText = buttonText;
    }

    public int getImageId() {
        return imageId;
    }

    public String getButtonText() {
        return buttonText;
    }
}

