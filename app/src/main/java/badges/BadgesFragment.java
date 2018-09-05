package badges;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.io.Serializable;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BadgesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BadgesFragment extends DialogFragment {
    public static final String badgesKey = "badges_list";



    public BadgesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment BadgesFragment.
     */
    public static BadgesFragment newInstance(List<Badge> badges) {
        BadgesFragment fragment = new BadgesFragment();
        Bundle args = new Bundle();
        args.putSerializable(badgesKey, (Serializable) badges);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_badges, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setTitle(R.string.Badges);

        Typeface robotoMedian = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Medium.ttf");


        BadgesAdapter badgesAdapter = new BadgesAdapter(getActivity(), R.layout.single_badge, (List<Badge>)getArguments().getSerializable(badgesKey));
        ListView listView = (ListView) view.findViewById(R.id.badges_list);


        listView.setAdapter(badgesAdapter);
        TextView closeBtn = (TextView) view.findViewById(R.id.badges_close_btn);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BadgesFragment.this.dismiss();
            }
        });
    }


}
