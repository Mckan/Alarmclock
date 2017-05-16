package com.dev.mckan.alarmclock;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;


public class AlarmListActivity extends ListActivity {

    private AlarmListAdapter mAdapter;
    private AlarmDBHelper dbHelper = new AlarmDBHelper(this);
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        requestWindowFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.activity_alarm_list);

        mAdapter = new AlarmListAdapter(this, dbHelper.getAlarms());

        setListAdapter(mAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_new_alarm) {
            startAlarmInfoActivity(-1);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            mAdapter.setAlarms(dbHelper.getAlarms());
            mAdapter.notifyDataSetChanged();
        }
    }

    // Enable alarm
    public void setAlarmEnabled(long id, boolean isEnabled) {
        AlarmManagerHelper.cancelAlarms(this);

        AlarmModel model = dbHelper.getAlarm(id);
        model.isEnabled = isEnabled;
        dbHelper.updateAlarm(model);

        AlarmManagerHelper.setAlarms(this);
    }

    // Start Activity
    public void startAlarmInfoActivity(long id) {
        Intent intent = new Intent(this, AlarmInfoActivity.class);
        intent.putExtra("id", id);
        startActivityForResult(intent, 0);
    }

    public void deletAlarm(long id) {
        final long alarmId = id;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please confirm").setTitle("Delete set?")
        .setCancelable(true).setNegativeButton("Cancel", null)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Cancel Alarms
                AlarmManagerHelper.cancelAlarms(mContext);
                //Delete alarm from DB by id
                dbHelper.deleteAlarm(alarmId);
                //Refresh the list of the alarms in the adaptor
                mAdapter.setAlarms(dbHelper.getAlarms());
                //Notify the adapter the data has changed
                mAdapter.notifyDataSetChanged();
                //Set the alarms
                AlarmManagerHelper.setAlarms(mContext);
            }
        }).show();

    }
}
