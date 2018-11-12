package trianglz.models;

import java.io.Serializable;

/**
 * Created by ${Aly} on 11/11/2018.
 */
public class Participant implements Serializable{
    public String name;
    public int threadId,userId;
    public String userAvatarUrl;

    public Participant(String name, int threadId, int userId, String userAvatarUrl) {
        this.name = name;
        this.threadId = threadId;
        this.userId = userId;
        this.userAvatarUrl = userAvatarUrl;
    }
}
