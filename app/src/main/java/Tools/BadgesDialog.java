package Tools;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.bedopedia.bedopedia_android.R;
import com.example.bedopedia.bedopedia_android.StudentActivity;

/**
 * Created by khaled on 3/14/17.
 */

public class BadgesDialog {
    public static  void AlertDialog(Context context){

        LayoutInflater inflater =  (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        new AlertDialog.Builder(context).setTitle("Badges")
                .setView(inflater.inflate(R.layout.badges_dialog, null))
                .setPositiveButton(android.R.string.ok
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }
}
