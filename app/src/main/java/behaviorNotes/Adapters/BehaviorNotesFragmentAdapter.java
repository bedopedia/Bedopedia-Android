package behaviorNotes.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import behaviorNotes.BehaviorNote;

import com.example.bedopedia.bedopedia_android.R;

import java.util.List;

import behaviorNotes.Fragments.NegativeFragment;
import behaviorNotes.Fragments.PositiveFragment;
import student.StudentFragment;


/**
 * Created by khaled on 3/9/17.
 */

public class BehaviorNotesFragmentAdapter extends FragmentPagerAdapter {
    List<BehaviorNote> positiveNotesList;
    List<BehaviorNote> negativeNotesList;
    Context context;
    public BehaviorNotesFragmentAdapter(FragmentManager fm, List<BehaviorNote> positiveNotesList,List<BehaviorNote> negativeNotesList, Context context) {
        super(fm);
        this.positiveNotesList = positiveNotesList;
        this.negativeNotesList = negativeNotesList;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment f = null;

        if (position == 0) {
            f = PositiveFragment.newInstance(positiveNotesList);
        } else {
            f =  NegativeFragment.newInstance(negativeNotesList);
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
        View view = LayoutInflater.from(context).inflate(R.layout.single_tab, null);
        TextView title = (TextView) view.findViewById(R.id.behavior_note_tab_title);
        TextView counter = (TextView) view.findViewById(R.id.behavior_note_tab_counter);
        title.setText(this.getPageTitle(position));
        if (position == 0) {
            counter.setText(positiveNotesList.size() + "");
            if(positiveNotesList.size() == 0)
                counter.setVisibility(View.INVISIBLE);
        }
        else {
            counter.setText(StudentFragment.negativeNotesList.size() + "");
            if(StudentFragment.negativeNotesList.size() == 0)
                counter.setVisibility(View.INVISIBLE);
        }

        return view;

    }

}
