package trianglz.models;

import java.io.Serializable;

/**
 * Created by ${Aly} on 11/11/2018.
 */
public class  Message implements Serializable {
    public Object attachmentUrl;
    public String body;
    public String createdAt;
    public Object ext;
    public Object fileName;
    public int id;
    public int threadId;
    public String updatedAT;
    public trianglz.models.User user;

    public Message(Object attachmentUrl, String body, String createdAt, Object ext, Object fileName,
                   int id, int threadId, String updatedAT, trianglz.models.User user) {
        this.attachmentUrl = attachmentUrl;
        this.body = body;
        this.createdAt = createdAt;
        this.ext = ext;
        this.fileName = fileName;
        this.id = id;
        this.threadId = threadId;
        this.updatedAT = updatedAT;
        this.user = user;
    }
}
