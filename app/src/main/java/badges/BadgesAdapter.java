package badges;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;

import java.util.List;

/**
 * Created by khaled on 3/14/17.
 */

public class BadgesAdapter extends ArrayAdapter<Badge> {

    public Context context;

    public BadgesAdapter(Context context, int resource, List<Badge> items) {
        super(context, resource, items);
        this.context = context;
    }

    public static  class Holder{
        ImageView badgeIcon;
        TextView badgeName;
        TextView badgeReason;
        TextView courseName;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // Get the data item for this position
        Badge badge = (Badge) getItem(position);
        Holder item;

        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.single_badge, parent, false);
        }
        item=new Holder();
        Typeface robotoMedium = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Medium.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Regular.ttf");

        item.badgeIcon = (ImageView) view.findViewById(R.id.badge_icon);
        item.badgeName = (TextView) view.findViewById(R.id.badge_name);
        item.badgeReason = (TextView) view.findViewById(R.id.badge_reason);
        item.courseName = (TextView) view.findViewById(R.id.badge_course_name);

        item.badgeName.setTypeface(robotoMedium);
        item.badgeReason.setTypeface(robotoRegular);
        item.courseName.setTypeface(robotoRegular);

        item.badgeName.setText(badge.getName());
        item.badgeReason.setText(badge.getReason());
        item.courseName.setText(badge.getCourseName());


        String imageName = badge.getIcon();
        int res = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        item.badgeIcon.setImageResource(res);

        return view;
    }
}


