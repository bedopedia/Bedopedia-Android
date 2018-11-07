package trianglz.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.List;

import trianglz.ui.fragments.NegativeFragment;
import trianglz.ui.fragments.PositiveFragment;
import trianglz.models.BehaviorNote;


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

        Fragment fragment = null;

        if (position == 0) {
            fragment = PositiveFragment.newInstance(positiveNotesList);
        } else {
            fragment =  NegativeFragment.newInstance(negativeNotesList);
        }

        return fragment;
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
        TextView tabTitle = (TextView) view.findViewById(R.id.behavior_note_tab_title);
        TextView behaviorCounter = (TextView) view.findViewById(R.id.behavior_note_tab_counter);
        tabTitle.setText(this.getPageTitle(position));
        if (position == 0) {
            behaviorCounter.setText(positiveNotesList.size() + "");
            if(positiveNotesList.size() == 0)
                behaviorCounter.setVisibility(View.INVISIBLE);
        }
        else {
            behaviorCounter.setText(negativeNotesList.size() + "");
            if(negativeNotesList.size() == 0)
                behaviorCounter.setVisibility(View.INVISIBLE);
        }
        return view;
    }

}
