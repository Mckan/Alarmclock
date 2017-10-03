package com.dev.mckan.alarmclock;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.*;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.ViewHolder> {

    private Context mContext;
    private List<AlarmModel> mAlarms;

    public AlarmListAdapter(Context context, List<AlarmModel> alarms) {
        this.mContext = context;
        this.mAlarms = alarms;
    }

    @Override
    public int getItemCount(){
        if(mAlarms != null){
            return mAlarms.size();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if(mAlarms != null) {
            return mAlarms.get(position).id;
        }
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.alarm_list_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AlarmModel model = mAlarms.get(position);

        holder.txtName.setText(model.name);
        holder.txtTime.setText(String.format("%02d : %02d", model.timeHour, model.timeMinute));
        holder.btnToggle.setChecked(model.isEnabled);
        holder.btnToggle.setTag(Long.valueOf(model.id));

        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AlarmListActivity) mContext).startAlarmInfoActivity(model.id);
            }
        });

    }


/*
    public View getView(int position, View view, ViewGroup parent){

        if (view == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.alarm_list_item, parent, false);
        }

        AlarmModel model = (AlarmModel) getItem(position);

        ANVÄNDER txtTime.setText(String.format("%02d : %02d", model.timeHour, model.timeMinute));

        ANVÄNDER txtName.setText(model.name);

        updateTextColor((TextView) view.findViewById(R.id.alarm_item_sunday), model.getRepeatingDay(AlarmModel.SUNDAY));
        updateTextColor((TextView) view.findViewById(R.id.alarm_item_monday), model.getRepeatingDay(AlarmModel.MONDAY));
        updateTextColor((TextView) view.findViewById(R.id.alarm_item_tuesday), model.getRepeatingDay(AlarmModel.TUESDAY));
        updateTextColor((TextView) view.findViewById(R.id.alarm_item_wednesday), model.getRepeatingDay(AlarmModel.WEDNESDAY));
        updateTextColor((TextView) view.findViewById(R.id.alarm_item_thursday), model.getRepeatingDay(AlarmModel.THURSDAY));
        updateTextColor((TextView) view.findViewById(R.id.alarm_item_friday), model.getRepeatingDay(AlarmModel.FRDIAY));
        updateTextColor((TextView) view.findViewById(R.id.alarm_item_saturday), model.getRepeatingDay(AlarmModel.SATURDAY));


        ANVÄNDER btnToggle.setChecked(model.isEnabled);
        ANVÄNDER btnToggle.setTag(Long.valueOf(model.id));

        btnToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((AlarmListActivity) mContext).setAlarmEnabled(((Long) buttonView.getTag()).longValue(), isChecked);
            }
        });

        view.setTag(Long.valueOf(model.id));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AlarmListActivity) mContext).startAlarmInfoActivity(((Long) v.getTag()).longValue());
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((AlarmListActivity) mContext).deleteAlarm(((Long) v.getTag()).longValue());
                return true;
            }
        });

        return view;
    } */

    public void setAlarms (List<AlarmModel> alarms) {
        mAlarms = alarms;
    }


    private void updateTextColor(TextView view, Boolean isOn)
    {
        if(isOn){
            view.setTextColor(Color.GREEN);
        } else {
            view.setTextColor(Color.RED);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView txtName;
        private TextView txtTime;
        private ToggleButton btnToggle;
        private View parentView;

        public ViewHolder(View view)
        {
            super(view);
            this.parentView = view;
            this.txtTime = (TextView) view.findViewById(R.id.alarm_item_time);
            this.txtName = (TextView) view.findViewById(R.id.alarm_item_name);
            this.btnToggle = (ToggleButton) view.findViewById(R.id.alarm_item_toggle);


        }

    }
}
