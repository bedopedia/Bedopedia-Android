package Tools;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;
import com.example.bedopedia.bedopedia_android.StudentActivity;
import com.example.bedopedia.bedopedia_android.TimetableActivity;

import java.util.List;

import Adapters.BadgesAdapter;
import Adapters.NotificationAdapter;
import Models.Badge;

/**
 * Created by khaled on 3/14/17.
 */

public class BadgesDialog {

    public static int getInDp(int dimensionInPixel, Context context){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionInPixel, context.getResources().getDisplayMetrics());
    }

    public static  void AlertDialog(Context context, List<Badge> badges){

        LayoutInflater inflater =  (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Typeface robotoMedian = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Medium.ttf");

        View v = inflater.inflate(R.layout.badges_dialog, null);

        BadgesAdapter badgesAdapter = new BadgesAdapter(context, R.layout.single_badge, badges);
        ListView listView = (ListView) v.findViewById(R.id.badges_list);
        TextView title = (TextView) v.findViewById(R.id.dialog_title);
        title.setTypeface(robotoMedian);

        listView.setAdapter(badgesAdapter);

        new AlertDialog.Builder(context)
                .setView(v)
                .setPositiveButton("CLOSE"
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }
}
