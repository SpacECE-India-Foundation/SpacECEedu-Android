package com.spacECE.spaceceedu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ActivityAdapter extends BaseAdapter {

    final String TAG = "ActivityAdapter";
    Context context;
    List<ActivityData> activityDataList;

    public ActivityAdapter(Context context,List<ActivityData> activityDataList){
        this.context = context;
        this.activityDataList = activityDataList;
    }

    @Override
    public int getCount() {
        return activityDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return activityDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_item,parent,false);

        ActivityData currActivityData = (ActivityData) getItem(position);
        List<Data> dataList = currActivityData.getData();
        TextView textViewActivityId,textViewActivityTitle;

        //textViewActivityId = convertView.findViewById(R.id.text_activity_id);
        textViewActivityTitle = convertView.findViewById(R.id.text_activity_title);

        //textViewActivityId.setText(currActivityData.getData().get(0).getActivityNo().toString());
        textViewActivityTitle.setText(currActivityData.getData().get(0).getActivityName().toString());

        return convertView;
    }
}
