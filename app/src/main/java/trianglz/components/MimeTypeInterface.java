package trianglz.components;

import java.util.ArrayList;

import trianglz.models.Message;

public interface MimeTypeInterface {
    void onCheckType(Message message, int position);
}
