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
import android.widget.ImageView;

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

    public static void getImageFromUrl(Context context,String url, ImageView imageView) {
        Picasso.with(context).load(ApiClient.BASE_URL+url).into(imageView);

    }
}
