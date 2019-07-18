package trianglz.models;//
//  UploadedObject.java
//
//  Generated using https://jsonmaster.github.io
//  Created on July 18, 2019
//

import java.util.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UploadedObject {

    @SerializedName("id")
    private int id;
    @SerializedName("download_name")
    private String downloadName;
    @SerializedName("content_type")
    private String contentType;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("file_size")
    private int fileSize;
    @SerializedName("downloads_number")
    private int downloadsNumber;
    @SerializedName("created_at")
    private int createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("original_filename")
    private String originalFilename;
    @SerializedName("extension")
    private String extension;
    @SerializedName("file_identifier")
    private String fileIdentifier;
    @SerializedName("url")
    private String url;
    @SerializedName("upload_type")
    private String uploadType;
    @SerializedName("creator_name")
    private String creatorName;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setDownloadName(String downloadName) {
        this.downloadName = downloadName;
    }

    public String getDownloadName() {
        return this.downloadName;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getFileSize() {
        return this.fileSize;
    }

    public void setDownloadsNumber(int downloadsNumber) {
        this.downloadsNumber = downloadsNumber;
    }

    public int getDownloadsNumber() {
        return this.downloadsNumber;
    }

    public void setCreatedAt(int createdAt) {
        this.createdAt = createdAt;
    }

    public int getCreatedAt() {
        return this.createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getOriginalFilename() {
        return this.originalFilename;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return this.extension;
    }

    public void setFileIdentifier(String fileIdentifier) {
        this.fileIdentifier = fileIdentifier;
    }

    public String getFileIdentifier() {
        return this.fileIdentifier;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
    }

    public String getUploadType() {
        return this.uploadType;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorName() {
        return this.creatorName;
    }


    public static UploadedObject create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, UploadedObject.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}