package com.eleco.model;


public class CommentData {
    private String idUser;
    private String idPost;
    private String idComment;
    private String imageUrl;
    private String userName;
    private String postText;
    private int likeCount;



    public CommentData() {
        // Konstruktor default
    }


    public CommentData(String idUser, String idPost, String idComment, String imageUrl, String userName, String postText, int likeCount) {
        this.idUser = idUser;
        this.idPost = idPost;
        this.imageUrl = imageUrl;
        this.userName = userName;
        this.postText = postText;
        this.likeCount = likeCount;
        this.idComment = idComment;


    }
    public String getIdComment() {
        return idComment;
    }

    public void setIdComment(String idComment) {
        this.idComment = idComment;
    }


    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public String getimageUrl() {
        return imageUrl;
    }

    public void setimageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

}

