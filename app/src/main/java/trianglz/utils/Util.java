package trianglz.utils;

import android.Manifest;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener;
import com.skolera.skolera_android.R;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import Tools.CalendarUtils;
import gun0912.tedbottompicker.TedBottomPicker;
import trianglz.components.DexterPermissionErrorListener;
import trianglz.components.MimeTypeInterface;
import trianglz.components.OnImageSelectedListener;

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

    public static void showErrorDialog(Context context, String title, String content) {
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


    public static void showNoInternetConnectionDialog(Context context) {
        showErrorDialog(context, context.getResources().getString(R.string.skolera), context.getResources().getString(R.string.no_internet_connection));
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static boolean isNotNullOrEmpty(String string) {
        return string != null && string.isEmpty() == false;
    }

    public static boolean isDateInside(String startString, String endString, String currentString) {
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


    public static String getCurrentDate() {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getDefault());
        Date dateObject = Calendar.getInstance().getTime();
        String date = df.format(dateObject);
        return date;
    }

    public static boolean isSameDay(String date1String, String date2String) {
        Date date1 = null;
        Date date2 = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            date1 = formatter.parse(date1String);
            date2 = formatter.parse(date2String);
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(date1);
            cal2.setTime(date2);
            return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                    cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static String getDate(String messageTime) {
        String finalData = "";
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        Date date = null;
        try {
            date = fmt.parse(messageTime);
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
            String[] dates = fmtOut.format(date).split(" ");
            Calendar cal = CalendarUtils.getCalendar(Calendar.getInstance().getTime());
            Integer day = cal.get(Calendar.DAY_OF_MONTH);
            String notificationDate = fmtOut.format(date);
            if (dates[0].equals(String.valueOf(day))) {
                notificationDate = "Today" + notificationDate.substring(dates[0].length() + dates[1].length() + 1);
            } else if (dates[0].equals(String.valueOf(day - 1))) {
                notificationDate = "Yesterday" + notificationDate.substring(dates[0].length() + dates[1].length() + 1);
            }
            finalData = notificationDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finalData;
    }


    public static Date convertStringToDate(String dateString) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date date = null;
        try {
            date = fmt.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }



    public static Date convertUtcToLocal(String dateString){
        String formattedDate = "";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(dateString);
            df.setTimeZone(TimeZone.getDefault());
            formattedDate = df.format(date);
            Date date1 = df.parse(formattedDate);
            return date1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(date == null){
            date = Calendar.getInstance().getTime();
        }
        return date;
    }


    public static String convertLocaleToUtc(String dateString){
        String formattedDate = "";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getDefault());
        Date date = null;
        try {
            date = df.parse(dateString);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            formattedDate = df.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getLocale(Context context) {
        Locale current = context.getResources().getConfiguration().locale;
        if (current.toString().contains("ar"))
            return "ar";
        else return "en";
    }

    public static void selectImagesFromGallery(final OnImageSelectedListener listener,
                                               final FragmentActivity activity,
                                               final ViewGroup rootView) {


        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {

                TedBottomPicker tedBottomPicker;


                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {

                    tedBottomPicker = new TedBottomPicker.Builder(activity)
                            .showCameraTile(false)
                            .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                                @Override
                                public void onImageSelected(Uri uri) {
                                    listener.onImagesSelected(uri);
                                }
                            })
                            .create();

                    tedBottomPicker.show(activity.getSupportFragmentManager());

                } else {


                    tedBottomPicker = new TedBottomPicker.Builder(activity)
                            .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                                @Override
                                public void onImageSelected(Uri uri) {
                                    listener.onImagesSelected(uri);
                                }
                            })
                            .create();

                    tedBottomPicker.show(activity.getSupportFragmentManager());
                }

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
            }

            @Override
            public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        };

        PermissionListener snackbarPermissionListener = SnackbarOnDeniedPermissionListener.Builder
                .with(rootView,"")
                .withOpenSettingsButton("")
                .build();

        PermissionListener writeExternalPermissionListener = new CompositePermissionListener(permissionListener,
                snackbarPermissionListener);

        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(writeExternalPermissionListener)
                .withErrorListener(new DexterPermissionErrorListener())
                .check();

    }


    public static void isImageUrl(final ArrayList<Object> messageArrayList, final MimeTypeInterface mimeTypeInterface) {
        final ArrayList<Object> filteredArrayList = new ArrayList<>();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < messageArrayList.size(); i++) {
                    if (messageArrayList.get(i) instanceof String) {
                        filteredArrayList.add(messageArrayList.get(i));
                    } else {
                        trianglz.models.Message message = (trianglz.models.Message) messageArrayList.get(i);
                        if (!message.attachmentUrl.isEmpty() && !message.attachmentUrl.equals("null")) {
                            URLConnection connection = null;
                            try {
                                connection = new URL(message.attachmentUrl).openConnection();
                                String contentType = connection.getHeaderField("Content-Type");
                                boolean isImage = contentType.startsWith("image/");
                                if (isImage) {
                                    filteredArrayList.add(messageArrayList.get(i));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            filteredArrayList.add(messageArrayList.get(i));
                        }

                    }
                }

                mimeTypeInterface.onCheckType(filteredArrayList);

            }
        });
    }
}
