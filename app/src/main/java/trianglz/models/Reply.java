package trianglz.models;

//
//  Reply.java
//
//  Generated using https://jsonmaster.github.io
//  Created on July 18, 2019
//

import java.util.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Reply {

    @SerializedName("id")
    private int id;
    @SerializedName("content")
    private String content;
    @SerializedName("post_id")
    private int postId;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("owner")
    private Owner owner;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getPostId() {
        return this.postId;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Owner getOwner() {
        return this.owner;
    }


    public static Reply create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, Reply.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}
