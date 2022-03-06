package trianglz.ui.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.skolera.skolera_android.R;

import java.util.ArrayList;
import java.util.List;

import trianglz.models.BehaviorNote;
import trianglz.ui.fragments.NegativeFragment;
import trianglz.ui.fragments.OtherFragment;
import trianglz.ui.fragments.PositiveFragment;


/**
 * Created by khaled on 3/9/17.
 */

public class BehaviorNotesFragmentAdapter extends FragmentPagerAdapter {
    List<BehaviorNote> positiveNotesList;
    List<BehaviorNote> negativeNotesList;
    List<BehaviorNote> otherNoteList;
    Context context;
    ArrayList<Fragment> fragmentList;
    ArrayList<String> namesArrayList = new ArrayList<>();
    public BehaviorNotesFragmentAdapter(FragmentManager fm, List<BehaviorNote> positiveNotesList,
                                        List<BehaviorNote> negativeNotesList,
                                        List<BehaviorNote> otherNotesList, Context context) {
        super(fm);
        this.positiveNotesList = positiveNotesList;
        this.negativeNotesList = negativeNotesList;
        this.otherNoteList = otherNotesList;
        this.context = context;
        this.fragmentList = getFragments();

    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return fragmentList.get(0);
        } else if(position == 1) {
            return fragmentList.get(1);
        }else {
            return fragmentList.get(2);
        }
    }

    @Override
    public int getCount() {

        return getFragments().size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0)
            return namesArrayList.get(0);
        else if(position == 1){
            return namesArrayList.get(1);
        }else {
            return namesArrayList.get(2);
        }

    }

    private ArrayList<Fragment> getFragments(){
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        if(positiveNotesList.size() > 0){
            fragmentArrayList.add( PositiveFragment.newInstance(positiveNotesList));
            namesArrayList.add(context.getResources().getString(R.string.positive));
        }

        if(negativeNotesList.size() > 0){
            fragmentArrayList.add( NegativeFragment.newInstance(negativeNotesList));
            namesArrayList.add(context.getResources().getString(R.string.negative));
        }

        if(otherNoteList.size() > 0){
            fragmentArrayList.add( OtherFragment.newInstance(otherNoteList));
            namesArrayList.add(context.getResources().getString(R.string.other));
        }

        return fragmentArrayList;
    }


}
