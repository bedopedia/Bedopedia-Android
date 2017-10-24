package badges;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skolera.skolera_android.R;

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

    public static  class BadgesHolder{
        ImageView badgeIcon;
        TextView badgeName;
        TextView badgeReason;
        TextView courseName;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // Get the data item for this position
        Badge badge = (Badge) getItem(position);
        BadgesHolder badgesHolderItem;

        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.single_badge, parent, false);
        }
        badgesHolderItem=new BadgesHolder();
        Typeface robotoMedium = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Medium.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Regular.ttf");

        badgesHolderItem.badgeIcon = (ImageView) view.findViewById(R.id.badge_icon);
        badgesHolderItem.badgeName = (TextView) view.findViewById(R.id.badge_name);
        badgesHolderItem.badgeReason = (TextView) view.findViewById(R.id.badge_reason);
        badgesHolderItem.courseName = (TextView) view.findViewById(R.id.badge_course_name);

        badgesHolderItem.badgeName.setTypeface(robotoMedium);
        badgesHolderItem.badgeReason.setTypeface(robotoRegular);
        badgesHolderItem.courseName.setTypeface(robotoRegular);

        badgesHolderItem.badgeName.setText(badge.getName());
        badgesHolderItem.badgeReason.setText(badge.getReason());
        badgesHolderItem.courseName.setText(badge.getCourseName());


        String imageName = badge.getIcon();
        int res = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        badgesHolderItem.badgeIcon.setImageResource(res);

        return view;
    }
}


