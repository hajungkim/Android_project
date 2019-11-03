package org.androidtown.probono;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//import com.example.user.cardoners.R;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    private List history;
    private Context context;

    public ListViewAdapter(List history, Context context) {
        this.history = history;
        this.context = context;
    }

    @Override
    public int getCount() {
        return history.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        TextView distanceTextView = (TextView) convertView.findViewById(R.id.distance) ;
        TextView timeTextView = (TextView) convertView.findViewById(R.id.time) ;
//        TextView ampmTextView = (TextView) convertView.findViewById(R.id.ampm) ;
        TextView start_addressTextView = (TextView) convertView.findViewById(R.id.start_address) ;
        TextView end_addressTextView = (TextView) convertView.findViewById(R.id.end_address) ;

        historyResource listViewItem = (historyResource) getItem(position);

        distanceTextView.setText(listViewItem.getDistance());
        timeTextView.setText(listViewItem.getTime());
//        ampmTextView.setText("temp");
        start_addressTextView.setText(listViewItem.getNow());
        end_addressTextView.setText(listViewItem.getDest());

        return convertView;
    }
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return history.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String distance, String time, String ampm, String start_address, String end_address) {
        ListViewItem item = new ListViewItem();

        item.setDistance(distance);
        item.setTime(time);
        item.setAmpm(ampm);
        item.setStart_address(start_address);
        item.setEnd_address(end_address);

        history.add(item);
    }
}