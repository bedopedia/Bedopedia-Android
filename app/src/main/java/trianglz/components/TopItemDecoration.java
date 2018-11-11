package trianglz.components;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ${Aly} on 11/11/2018.
 */
public class TopItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private boolean isGrid;

    public TopItemDecoration(int space, boolean isGrid) {
        this.space = space;
        this.isGrid = isGrid;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int dataSize = state.getItemCount();
        int position = parent.getChildAdapterPosition(view);
        if(isGrid){
            if(dataSize>0){
                if(position == 0){
                    outRect.set(0, space, 0,0 );
                }else if(position == 1){
                    outRect.set(0, space, 0,0 );
                }
            }
        }else {
            if(dataSize>0){
                if(position == 0) {
                    outRect.set(0, space, 0, 0);
                }
            }
        }

    }
}