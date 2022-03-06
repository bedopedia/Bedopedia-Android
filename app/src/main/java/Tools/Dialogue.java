package Tools;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
/**
 * Created by khaled on 2/21/17.
 */

public class Dialogue {
    public static  void AlertDialog(Context context, String title, String Message){
        new AlertDialog.Builder(context).setTitle(title)
                .setMessage(Message)
                .setPositiveButton(android.R.string.ok
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }
}
