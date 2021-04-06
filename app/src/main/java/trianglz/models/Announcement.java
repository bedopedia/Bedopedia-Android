package trianglz.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Announcement implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("body")
    private String body;
    @SerializedName("end_at")
    private String endAt;
    @SerializedName("admin_id")
    private int adminId;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("children_names")
    private Object childrenNames;
    @SerializedName("announcement_receivers")
    private AnnouncementReceivers[] announcementReceivers;
    @SerializedName("subscriptions")
    private Subscriptions[] subscriptions;
    @SerializedName("uploaded_files")
    private UploadedObject[] uploadedFiles;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return this.body;
    }

    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public String getEndAt() {
        return this.endAt;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public int getAdminId() {
        return this.adminId;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setChildrenNames(Object childrenNames) {
        this.childrenNames = childrenNames;
    }

    public Object getChildrenNames() {
        return this.childrenNames;
    }

    public void setAnnouncementReceivers(AnnouncementReceivers[] announcementReceivers) {
        this.announcementReceivers = announcementReceivers;
    }

    public AnnouncementReceivers[] getAnnouncementReceivers() {
        return this.announcementReceivers;
    }

    public void setSubscriptions(Subscriptions[] subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Subscriptions[] getSubscriptions() {
        return this.subscriptions;
    }

    public void setUploadedFiles(UploadedObject[] uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }

    public UploadedObject[] getUploadedFiles() {
        return this.uploadedFiles;
    }


    public static Announcement create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, Announcement.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    public PostDetails toPostDetails() {
        PostDetails postDetails = new PostDetails();
        postDetails.setContent(body);
        postDetails.setId(id);
        Owner owner = new Owner();
        owner.name = title;
        owner.nameWithTitle = title;
        owner.avatarUrl = title;
        postDetails.setOwner(owner);
        postDetails.setCreatedAt(createdAt);
        postDetails.setComments(new Reply[0]);
        postDetails.setUploadedFiles(uploadedFiles);
        postDetails.wasAnnouncement = true;
        return postDetails;
    }
}
