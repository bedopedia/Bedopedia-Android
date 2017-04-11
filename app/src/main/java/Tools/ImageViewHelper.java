package Tools;

/**
 * Created by mo2men on 07/03/17.
 * to use
 *   ImageView logo = (ImageView)findViewById(R.id.imageView1);
    logo.setImageBitmap(ImageViewHelper.getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logo_icon),53));
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import login.Services.ApiClient;

public class ImageViewHelper {
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /*
        This Function is used to make you able to use picasso lib
        params : Context,
        The url of the image,
        ImageView instance
    */
    public static void getImageFromUrl(Context context,String url, ImageView imageView) {
        Picasso.with(context).load(ApiClient.BASE_URL+url).into(imageView);

    }
    /*
        This Function is used to make you able to use picasso lib
        params : Context,
        The url of the image,
        ImageView instance,
        Callback instance to deal with the case of failure
     */

    public static void getImageFromUrlWithCallback(Context context, String url, ImageView imageView, Callback callback) {
        Picasso.with(context).load(url).into(imageView, callback);

    }

    /*
        This Function is used to make you able to use picasso lib
        params : Context,
        The url of the image,
        ImageView instance
        Int id of backup  image
     */

    public static void getImageFromUrlWithIdFailure(Context context, String url, ImageView imageView, int id) {
        Picasso.with(context)
                .load(url)
                .error(id)
                .into(imageView);

    }


}
