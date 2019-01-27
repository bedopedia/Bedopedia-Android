package trianglz.components;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class BottomItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private boolean isGrid;

    public BottomItemDecoration(int space, boolean isGrid) {
        this.space = space;
        this.isGrid = isGrid;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int dataSize = state.getItemCount();
        int position = parent.getChildAdapterPosition(view);
        if (dataSize > 0) {
            if (isGrid) {
                if (dataSize % 2 == 0) {
                    if (position == dataSize - 1) {
                        outRect.set(0, 0, 0, space);
                    }  if (position == dataSize - 2) {
                        outRect.set(0, 0, 0, space);
                    }
                } else {
                    if (position == dataSize - 1) {
                        outRect.set(0, 0, 0, space);
                    }
                }
            }
            else {
                if (position == dataSize - 1) {
                    outRect.set(0, 0, 0, space);
                }
            }
        }


    }
}