package trianglz.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import trianglz.models.Message;


/**
 * Created by ${Aly} on 11/12/2018.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ME = 0;
    private static final int TYPE_OTHER = 1;
    private static final int TYPE_DATE = 2;

    public List<Object> mDataList;

    private Context context;
    private String userId;


    public ChatAdapter(Context context, String userId) {

        this.context = context;
        mDataList = new ArrayList<>();
        this.userId = userId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        if (viewType == TYPE_ME) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_me, parent, false);
            return new MeViewHolder(view);
        } else if (viewType == TYPE_OTHER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_other, parent, false);
            return new OtherViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_time, parent, false);
            return new TimeViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(mDataList.get(position) instanceof String){
            // TODO: 11/12/2018 add dates
        }else {
            Message message = (Message) mDataList.get(position);
            String body = android.text.Html.fromHtml(message.body).toString();
            body = StringEscapeUtils.unescapeJava(body);
            if(userId.equals(String.valueOf(message.user.getId()))){
                MeViewHolder meViewHolder = ((MeViewHolder)holder);
                meViewHolder.bodyTextView.setText(body);
            }else {
                OtherViewHolder otherViewHolder = ((OtherViewHolder)holder);
                otherViewHolder.bodyTextView.setText(body);
            }
        }

    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataList.get(position) instanceof String) {
            return TYPE_DATE;
        } else {
            Message message = (Message) mDataList.get(position);
            if (userId.equals(String.valueOf(message.user.getId()))) {
                return TYPE_ME;
            } else {
                return TYPE_OTHER;
            }
        }

    }

    public void addData(ArrayList<Object> data) {
        mDataList.clear();
        mDataList.addAll(data);
        notifyDataSetChanged();
    }


    private class OtherViewHolder extends RecyclerView.ViewHolder {
        public TextView bodyTextView;
        public OtherViewHolder(View itemView) {
            super(itemView);
            bodyTextView = itemView.findViewById(R.id.tv_body);
        }
    }

    private class MeViewHolder extends RecyclerView.ViewHolder {
        public TextView bodyTextView;
        public MeViewHolder(View itemView) {
            super(itemView);
            bodyTextView = itemView.findViewById(R.id.tv_body);
        }
    }


    private class TimeViewHolder extends RecyclerView.ViewHolder {
        public TextView timeTextView;

        public TimeViewHolder(View itemView) {
            super(itemView);

        }
    }

    private String setMessageTime(long timeStamp) {
        String date = "";
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeStamp * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm a", Locale.ENGLISH);
        date = simpleDateFormat.format(cal.getTime());
        return date;
    }


}