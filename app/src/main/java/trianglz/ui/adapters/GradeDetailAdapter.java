package trianglz.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.models.Assignments;
import trianglz.models.GradeHeader;
import trianglz.models.GradeItems;
import trianglz.models.GradesDetailsResponse;
import trianglz.models.GradingPeriod;
import trianglz.models.Quizzes;
import trianglz.models.SubGradingPeriod;

/**
 * Created by ${Aly} on 11/7/2018.
 */
public class GradeDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Object> mDataList;
    private Context context;


    private static final int TYPE_SEMESTER_QUARTER_HEADER = 0;
    private static final int TYPE_GRADE_HEADER = 2;
    private static final int TYPE_GRADE = 1;

    public GradeDetailAdapter(Context context) {
        this.context = context;
        mDataList = new ArrayList<>();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SEMESTER_QUARTER_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_quarter, parent, false);
            return new QuarterViewHolder(view);
        } else if (viewType == TYPE_GRADE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_detail, parent, false);
            return new DetailViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        Object item = mDataList.get(position);
        if (getItemViewType(position) == TYPE_SEMESTER_QUARTER_HEADER) {
            QuarterViewHolder quarterViewHolder = (QuarterViewHolder) holder;
            GradeHeader gradeHeader = (GradeHeader) item;
            quarterViewHolder.quarterTextView.setText(gradeHeader.header);
        } else if (getItemViewType(position) == TYPE_GRADE_HEADER) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            GradeHeader gradeHeader = (GradeHeader) item;
            headerViewHolder.headerTextView.setText(gradeHeader.header);
        } else {
            DetailViewHolder detailViewHolder = (DetailViewHolder) holder;
            if (item instanceof GradeItems) {
                GradeItems grade = (GradeItems) item;
                detailViewHolder.classWorkTextView.setText(grade.name);
                detailViewHolder.markTextView.setText(String.format(context.getString(R.string.mark),
                        String.valueOf(grade.grade),
                        String.valueOf(grade.total)));
            } else if (item instanceof Quizzes) {
                Quizzes grade = (Quizzes) item;
                detailViewHolder.classWorkTextView.setText(grade.getName());
                detailViewHolder.markTextView.setText(String.format(context.getString(R.string.mark),
                        String.valueOf(grade.getGrade()),
                        String.valueOf(grade.getTotal())));
            } else {
                Assignments grade = (Assignments) item;
                detailViewHolder.classWorkTextView.setText(grade.name);
                detailViewHolder.markTextView.setText(String.format(context.getString(R.string.mark),
                        String.valueOf(grade.grade),
                        String.valueOf(grade.total)));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = mDataList.get(position);
        if (item instanceof GradeHeader) {
            GradeHeader gradeHeader = (GradeHeader) item;
            if (gradeHeader.headerType == GradeHeader.HeaderType.SEMESTER) {
                return TYPE_SEMESTER_QUARTER_HEADER;
            } else {
                return TYPE_GRADE_HEADER;
            }
        } else {
            return TYPE_GRADE;
        }
    }

    public void addData(GradesDetailsResponse gradesDetailsResponse) {
        mDataList.clear();
        mDataList.addAll(sortGradingPeriodsArray(gradesDetailsResponse.gradingPeriods));
        notifyDataSetChanged();
    }

    private ArrayList<Object> sortGradingPeriodsArray(ArrayList<GradingPeriod> gradingPeriods) {
        ArrayList<Object> array = new ArrayList<>();
        for (GradingPeriod gradingPeriod : gradingPeriods) {
            array.add(getGradeHeader(gradingPeriod.name, GradeHeader.HeaderType.SEMESTER));
            if (gradingPeriod.gradeItems.size() != 0) {
                array.add(getGradeHeader("Grade items", GradeHeader.HeaderType.GRADE));
                array.addAll(gradingPeriod.gradeItems);
            }
            if (gradingPeriod.quizzes.size() != 0) {
                array.add(getGradeHeader("Quizzes", GradeHeader.HeaderType.GRADE));
                array.addAll(gradingPeriod.quizzes);
            }
            if (gradingPeriod.assignments.size() != 0) {
                array.add(getGradeHeader("Assignments", GradeHeader.HeaderType.GRADE));
                array.addAll(gradingPeriod.assignments);
            }
            if (gradingPeriod.subGradingPeriods.size() != 0) {
                for (SubGradingPeriod subGradingPeriod : gradingPeriod.subGradingPeriods) {
                    array.add(getGradeHeader(subGradingPeriod.name, GradeHeader.HeaderType.SEMESTER));
                    if (subGradingPeriod.gradeItems.size() != 0) {
                        array.add(getGradeHeader("Grade items", GradeHeader.HeaderType.GRADE));
                        array.addAll(subGradingPeriod.gradeItems);
                    }
                    if (subGradingPeriod.quizzes.size() != 0) {
                        array.add(getGradeHeader("Quizzes", GradeHeader.HeaderType.GRADE));
                        array.addAll(subGradingPeriod.quizzes);
                    }
                    if (subGradingPeriod.assignments.size() != 0) {
                        array.add(getGradeHeader("Assignments", GradeHeader.HeaderType.GRADE));
                        array.addAll(subGradingPeriod.assignments);
                    }
                }
            }
        }
        return array;
    }

    private GradeHeader getGradeHeader(String name, GradeHeader.HeaderType headerType) {
        GradeHeader gradeHeader = new GradeHeader();
        gradeHeader.header = name;
        gradeHeader.headerType = headerType;
        return gradeHeader;
    }

    public class QuarterViewHolder extends RecyclerView.ViewHolder {
        public TextView quarterTextView;

        public QuarterViewHolder(View itemView) {
            super(itemView);
            quarterTextView = itemView.findViewById(R.id.tv_quarter);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTextView, headerGradeTextView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerTextView = itemView.findViewById(R.id.tv_header);
            headerGradeTextView = itemView.findViewById(R.id.tv_total_header_grade);
        }
    }


    public class DetailViewHolder extends RecyclerView.ViewHolder {
        public TextView classWorkTextView, markTextView, averageGradeTextView, stateTextView;

        public DetailViewHolder(View itemView) {
            super(itemView);
            classWorkTextView = itemView.findViewById(R.id.tv_class_work);
            markTextView = itemView.findViewById(R.id.tv_mark);
            averageGradeTextView = itemView.findViewById(R.id.tv_avg_grade);
            stateTextView = itemView.findViewById(R.id.tv_state);
        }
    }
}