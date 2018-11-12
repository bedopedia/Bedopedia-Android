package trianglz.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.skolera.skolera_android.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ${Aly} on 10/24/2018.
 */
public class Util {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static Drawable getCurvedBackgroundColor(float radius, int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        float[] statusRadii = new float[]{radius, radius, radius, radius, radius, radius, radius, radius}; // For Corners
        drawable.setCornerRadii(statusRadii);
        drawable.setColor(color);
        return drawable;
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static void showErrorDialog(Context context, String title, String content){
        new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .titleColor(context.getResources().getColor(R.color.jade_green))
                .neutralText("Ok")
                .neutralColor(context.getResources().getColor(R.color.jade_green))
                .contentColor(context.getResources().getColor(R.color.steel))
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {

                    }
                })
                .show();
    }


    public static void showNoInternetConnectionDialog(Context context){
        showErrorDialog(context,context.getResources().getString(R.string.skolera),context.getResources().getString(R.string.no_internet_connection));
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static boolean isNotNullOrEmpty(String string) {
        return string != null && string.isEmpty() == false;
    }

    public static boolean isDateInside(String startString, String endString, String currentString){
            Date start = null;
            Date end = null;
            Date current = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
             start = formatter.parse(startString);
             end = formatter.parse(endString);
             current = formatter.parse(currentString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return current.compareTo(start) >= 0 && current.compareTo(end) <= 0;
    }


    public static String getCurrentDate(){
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);
        return reportDate;
    }
}
