package trianglz.models;

/**
 * Created by ${Aly} on 11/12/2018.
 */
public class MessageAttributes {
    public String id;
    public String body;
    public String attachment;

    public MessageAttributes(String id, String body, String attachment) {
        this.id = id;
        this.body = body;
        this.attachment = attachment;
    }
}
