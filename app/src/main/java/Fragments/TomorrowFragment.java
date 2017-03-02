package Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bedopedia.bedopedia_android.R;

/**
 * Created by khaled on 3/2/17.
 */

public class TomorrowFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tomorrow_fragment, container, false);

        return rootView;
    }
}
