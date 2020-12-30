package edu.cuny.light.Models;

public class Post {
    private String postID;
    private String ownerID;
    private String content;


    public Post(){

    }

    public Post(String postID, String ownerID, String content) {
        this.postID=postID;
        this.ownerID = ownerID;
        this.content = content;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
