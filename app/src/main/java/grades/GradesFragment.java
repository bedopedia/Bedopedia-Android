package grades;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.skolera.skolera_android.R;

import java.io.Serializable;
import java.util.List;

public class GradesFragment extends Fragment {

    private List<CourseGroup> courseGroups;
    private String studentId ;

    public GradesFragment() {
    }

    public static Fragment newInstance(List<CourseGroup> courseGroups , String studentId) {
        Fragment gradesFragment = new GradesFragment();
        Bundle args = new Bundle();
        args.putSerializable("grades" , (Serializable) courseGroups);
        args.putString("studentId",studentId);
        gradesFragment.setArguments(args);
        return gradesFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courseGroups = (List<CourseGroup>) getArguments().getSerializable("grades");
            studentId = getArguments().getString("studentId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grades, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        GradesAdapter adapter = new GradesAdapter(getActivity(), R.layout.single_grade, courseGroups,studentId);
        ListView gradesList = (ListView) view.findViewById(R.id.grades_list);
        gradesList.setAdapter(adapter);


    }
}
