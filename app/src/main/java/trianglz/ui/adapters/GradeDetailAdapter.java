package trianglz.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;

import trianglz.models.Assignments;
import trianglz.models.Category;
import trianglz.models.GradeBook;
import trianglz.models.GradeHeader;
import trianglz.models.GradeItems;
import trianglz.models.Quizzes;
import trianglz.models.Subcategory;
import trianglz.utils.Util;

/**
 * Created by ${Aly} on 11/7/2018.
 */
public class GradeDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Object> mDataList;
    private ArrayList<Category> parentArray;
    private Context context;
    private GradeDetailsAdapterInterface gradeDetailsAdapterInterface;


    private static final int TYPE_SEMESTER_QUARTER_HEADER = 0;
    private static final int TYPE_GRADE_HEADER = 2;
    private static final int TYPE_GRADE = 1;

    public GradeDetailAdapter(Context context, GradeDetailsAdapterInterface gradeDetailsAdapterInterface) {
        this.context = context;
        mDataList = new ArrayList<>();
        parentArray= new ArrayList<>();
        this.gradeDetailsAdapterInterface = gradeDetailsAdapterInterface;

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
            if (gradeHeader.headerType == GradeHeader.HeaderType.CATEGORY) {
                quarterViewHolder.itemView.setBackgroundResource(R.color.category);
            } else if (gradeHeader.headerType == GradeHeader.HeaderType.SUBCATEGORY) {
                quarterViewHolder.itemView.setBackgroundResource(R.color.subcategory);
            } else if (gradeHeader.headerType == GradeHeader.HeaderType.SUBCATEGORY_TOTAL) {
                quarterViewHolder.itemView.setBackgroundResource(R.color.subcategory_total);
            } else if (gradeHeader.headerType == GradeHeader.HeaderType.TOTAL) {
                quarterViewHolder.itemView.setBackgroundResource(R.color.total);
            } else if (gradeHeader.headerType == GradeHeader.HeaderType.CATEGORY_TOTAL) {
                quarterViewHolder.itemView.setBackgroundResource(R.color.category);
            }
            quarterViewHolder.gradeTextview.setText(gradeHeader.gradeText);
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
                detailViewHolder.markTextView.setText(getMarkText(grade.gradeView, grade.total));
            } else if (item instanceof Quizzes) {
                Quizzes grade = (Quizzes) item;
                detailViewHolder.classWorkTextView.setText(grade.getName());
                detailViewHolder.markTextView.setText(getMarkText(grade.getGradeView(), grade.getTotal()));
            } else {
                Assignments grade = (Assignments) item;
                detailViewHolder.classWorkTextView.setText(grade.name);
                detailViewHolder.markTextView.setText(getMarkText(grade.gradeView, grade.total));
            }
        }
    }

    private String getMarkText(String gradeView, double total) {
        if (hideTotalGrade(gradeView)) {
            return gradeView;
        } else {
            return String.format(context.getString(R.string.mark),
                    Util.removeZeroDecimal(gradeView),
                    Util.removeZeroDecimal(String.valueOf(total)));
        }
    }
    /**
     * @param string grade view to check if we should hide the total grade
     */
    private boolean hideTotalGrade(String string) {
        if (string == null) return false;
        return !NumberUtils.isParsable(string) && !string.contains("*");
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
            if (gradeHeader.headerType == GradeHeader.HeaderType.CATEGORY ||
                    gradeHeader.headerType == GradeHeader.HeaderType.SUBCATEGORY ||
                    gradeHeader.headerType == GradeHeader.HeaderType.SUBCATEGORY_TOTAL ||
                    gradeHeader.headerType == GradeHeader.HeaderType.CATEGORY_TOTAL ||
                    gradeHeader.headerType == GradeHeader.HeaderType.TOTAL) {
                return TYPE_SEMESTER_QUARTER_HEADER;
            } else {
                return TYPE_GRADE_HEADER;
            }
        } else {
            return TYPE_GRADE;
        }
    }

    public void addData(GradeBook gradeBook) {
        mDataList.clear();
        parentArray = gradeBook.categories;
        mDataList.addAll(sortGradingPeriodsArray(gradeBook, false));
        notifyDataSetChanged();
    }

    public void filterData(boolean currentSemester) {
//        mDataList.clear();
//        mDataList.addAll(sortGradingPeriodsArray(parentArray, currentSemester));
//        notifyDataSetChanged();
    }

    private ArrayList<Object> sortGradingPeriodsArray(GradeBook gradeBook, boolean currentSemester) {
        ArrayList<Category> categories = gradeBook.categories;
        ArrayList<Object> array = new ArrayList<>();
        for (Category category : categories) {
//            if (currentSemester) {
//                DateTime startDate, endDate, now;
//                startDate = new DateTime(category.startDate);
//                endDate = new DateTime(category.endDate);
//                now = new DateTime();
//                if (now.isBefore(startDate) || now.isAfter(endDate)) {
//                    continue;
//                }
//            }
            array.add(getGradeHeader(category.getName(), GradeHeader.HeaderType.CATEGORY,
                    getGradeString(category.gradeView, String.valueOf(category.getWeight()))));
            if (category.assignments != null && category.assignments.size() != 0) {
                array.add(getGradeHeader("Assignments", GradeHeader.HeaderType.GRADE,"" ));
                array.addAll(category.assignments);
            }
            if (category.quizzes != null && category.quizzes.size() != 0) {
                array.add(getGradeHeader("Quizzes", GradeHeader.HeaderType.GRADE,""));
                array.addAll(category.quizzes);
            }
            if (category.gradeItems != null && category.gradeItems.size() != 0) {
                array.add(getGradeHeader("Grade items", GradeHeader.HeaderType.GRADE,""));
                array.addAll(category.gradeItems);
            }
            if (category.subCategories != null && category.subCategories.size() != 0) {
                for (Subcategory subcategory : category.subCategories) {
//                    if (currentSemester) {
//                        DateTime startDate, endDate, now;
//                        startDate = new DateTime(subcategory.startDate);
//                        endDate = new DateTime(subcategory.endDate);
//                        now = new DateTime();
//                        if (now.isBefore(startDate) || now.isAfter(endDate)) {
//                            continue;
//                        }
//                    }
                    array.add(getGradeHeader(subcategory.name, GradeHeader.HeaderType.SUBCATEGORY,
                            getGradeString(subcategory.gradeView, String.valueOf(subcategory.weight))));
                    if (subcategory.assignments != null && subcategory.assignments.size() != 0) {
                        array.add(getGradeHeader("Assignments", GradeHeader.HeaderType.GRADE,""));
                        array.addAll(subcategory.assignments);
                    }
                    if (subcategory.quizzes != null && subcategory.quizzes.size() != 0) {
                        array.add(getGradeHeader("Quizzes", GradeHeader.HeaderType.GRADE,""));
                        array.addAll(subcategory.quizzes);
                    }
                    if (subcategory.gradeItems != null && subcategory.gradeItems.size() != 0) {
                        array.add(getGradeHeader("Grade items", GradeHeader.HeaderType.GRADE,""));
                        array.addAll(subcategory.gradeItems);
                    }
                    array.add(getGradeHeader(context.getString(R.string.total),
                            GradeHeader.HeaderType.SUBCATEGORY_TOTAL,
                            getGradeString(subcategory.grade, String.valueOf(subcategory.total))));
                }
            }
            array.add(getGradeHeader(context.getString(R.string.category_total),
                    GradeHeader.HeaderType.CATEGORY_TOTAL,
                    getGradeString(category.grade, String.valueOf(category.total))));
        }
        array.add(getGradeHeader(context.getString(R.string.total_percent), GradeHeader.HeaderType.TOTAL, gradeBook.grade + " %"));
        array.add(getGradeHeader(context.getString(R.string.letter_scale), GradeHeader.HeaderType.TOTAL, gradeBook.letterScale));
        if (!gradeBook.gpaScale.contains("--")) {
            array.add(getGradeHeader(context.getString(R.string.gpa), GradeHeader.HeaderType.TOTAL, gradeBook.gpaScale));
        }
        if (array.isEmpty()) {
            gradeDetailsAdapterInterface.arrayStatus(true);
        } else {
            for (Object object : array) {
                if (!(object instanceof GradeHeader)) {
                    gradeDetailsAdapterInterface.arrayStatus(false);
                    return array;
                }
            }
            gradeDetailsAdapterInterface.arrayStatus(true);
        }
        return array;
    }

    private String getGradeString(String grade, String total) {
        if (grade.toLowerCase().contains("n")) {
            return grade;
        }
        if (grade.contains(".0") && grade.lastIndexOf("0") == (grade.length() - 1)) {
            grade = grade.replace(".0", "");
        }
        if (total.contains(".0") && total.lastIndexOf("0") == (total.length() - 1)) {
            total = total.replace(".0", "");
        }
        return grade + "/" + total;

    }
    private GradeHeader getGradeHeader(String name, GradeHeader.HeaderType headerType, String gradeText) {
        GradeHeader gradeHeader = new GradeHeader();
        gradeHeader.header = name;
        gradeHeader.headerType = headerType;
        gradeHeader.gradeText = gradeText;
        return gradeHeader;
    }

    public class QuarterViewHolder extends RecyclerView.ViewHolder {
        public TextView quarterTextView, gradeTextview;

        public QuarterViewHolder(View itemView) {
            super(itemView);
            quarterTextView = itemView.findViewById(R.id.tv_quarter);
            gradeTextview = itemView.findViewById(R.id.tv_grade);
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

    public interface GradeDetailsAdapterInterface {
        void arrayStatus(boolean isEmpty);
    }
}