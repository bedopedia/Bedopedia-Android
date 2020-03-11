package trianglz.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.models.AttendanceTimetableSlot;

/**
 * Created by Farah A. Moniem on 15/09/2019.
 */
public class PerSlotAdapter extends RecyclerView.Adapter<PerSlotAdapter.ViewHolder> {

    private Context context;
    private ArrayList<AttendanceTimetableSlot> mDataList;
    private int selectedPosition = -1;
    private SlotAdapterInterface slotAdapterInterface;

    public PerSlotAdapter(Context context, SlotAdapterInterface slotAdapterInterface) {
        this.context = context;
        this.slotAdapterInterface = slotAdapterInterface;
        mDataList = new ArrayList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_slot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        viewHolder.clearAllSelected();
        if (selectedPosition == position) {
            viewHolder.itemLayout.setBackground(context.getResources().getDrawable(R.color.ice_blue_70,null));
            viewHolder.slotSelectedImageView.setVisibility(View.VISIBLE);
            viewHolder.slotDeselectedImageView.setVisibility(View.GONE);
        } else {
            viewHolder.slotSelectedImageView.setVisibility(View.GONE);
            viewHolder.slotDeselectedImageView.setVisibility(View.VISIBLE);
            viewHolder.itemLayout.setBackground(context.getResources().getDrawable(R.color.white,null));

        }
        viewHolder.slotNameTextView.setText(context.getResources().getString(R.string.slot) + " " + mDataList.get(position).getSlotNo());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addData(ArrayList<AttendanceTimetableSlot> list) {
        this.mDataList.clear();
        this.mDataList.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private FrameLayout itemLayout;
        private TextView slotNameTextView;
        private ImageView slotSelectedImageView, slotDeselectedImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.slot_layout);
            slotNameTextView = itemView.findViewById(R.id.slot_name);
            slotSelectedImageView = itemView.findViewById(R.id.slot_selected_imageview);
            slotDeselectedImageView = itemView.findViewById(R.id.slot_deselected_imageview);
            itemLayout.setOnClickListener(this);
        }

        public void clearAllSelected() {
            slotSelectedImageView.setVisibility(View.GONE);
            slotDeselectedImageView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.slot_layout) {
                selectedPosition = getAdapterPosition();
                slotAdapterInterface.onSlotClicked(mDataList.get(getAdapterPosition()));
                notifyDataSetChanged();

            }
        }
    }

    public interface SlotAdapterInterface {
        void onSlotClicked(AttendanceTimetableSlot timetableSlot);
    }
}
