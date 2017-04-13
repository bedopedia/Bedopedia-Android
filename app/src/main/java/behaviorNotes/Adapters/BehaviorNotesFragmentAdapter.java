package behaviorNotes.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import behaviorNotes.BehaviorNotesActivity;
import com.example.bedopedia.bedopedia_android.R;
import com.example.bedopedia.bedopedia_android.StudentActivity;

import java.io.Serializable;

import behaviorNotes.Fragments.NegativeFragment;
import behaviorNotes.Fragments.PositiveFragment;

/**
 * Created by khaled on 3/9/17.
 */

public class BehaviorNotesFragmentAdapter extends FragmentPagerAdapter {
    public BehaviorNotesFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment f = null;

        if (position == 0) {
            f = new PositiveFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("positiveNotesList", (Serializable) StudentActivity.positiveNotesList);
            f = new PositiveFragment();
            f.setArguments(bundle);
        } else {
            f = new NegativeFragment();
        }

        return f;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0)
            return "POSITIVE";
        return "NEGATIVE";
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(BehaviorNotesActivity.context).inflate(R.layout.single_tab, null);
        TextView title = (TextView) view.findViewById(R.id.tab_title);
        TextView counter = (TextView) view.findViewById(R.id.tab_counter);
        title.setText(this.getPageTitle(position));
        if (position == 0) {
            counter.setText(StudentActivity.positiveNotesList.size() + "");
            if(StudentActivity.positiveNotesList.size() == 0)
                counter.setVisibility(View.INVISIBLE);
        }
        else {
            counter.setText(StudentActivity.negativeNotesList.size() + "");
            if(StudentActivity.negativeNotesList.size() == 0)
                counter.setVisibility(View.INVISIBLE);
        }

        return view;

    }

}
