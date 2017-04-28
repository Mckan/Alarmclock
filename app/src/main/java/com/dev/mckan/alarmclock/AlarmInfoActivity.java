package com.dev.mckan.alarmclock;

import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;


public class AlarmInfoActivity extends ActionBarActivity {

private AlarmModel alarmDetails;
private AlarmDBHelper dbHelper = new AlarmDBHelper(this);

private TimePicker timePicker;
private EditText edtName;
private CustomToggleButton chkWeekly;
private CustomToggleButton chkSunday;
private CustomToggleButton chkMonday;
private CustomToggleButton chkTuesday;
private CustomToggleButton chkWednesday;
private CustomToggleButton chkThursday;
private CustomToggleButton chkFriday;
private CustomToggleButton chkSaturday;
private TextView txtToneSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set title on actionbar
        getSupportActionBar().setTitle("Create New Alarm");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_details);

        // Link view to variable
        timePicker = (TimePicker) findViewById(R.id.alarm_details_time_picker);
        edtName = (EditText) findViewById(R.id.alarm_details_name);
        chkWeekly = (CustomToggleButton) findViewById(R.id.alarm_details_repeat_weekly);
        chkSunday = (CustomToggleButton) findViewById(R.id.alarm_details_repeat_sunday);
        chkMonday = (CustomToggleButton) findViewById(R.id.alarm_details_repeat_monday);
        chkTuesday = (CustomToggleButton) findViewById(R.id.alarm_details_repeat_tuesday);
        chkWednesday = (CustomToggleButton) findViewById(R.id.alarm_details_repeat_wednesday);
        chkThursday = (CustomToggleButton) findViewById(R.id.alarm_details_repeat_thursday);
        chkFriday = (CustomToggleButton) findViewById(R.id.alarm_details_repeat_friday);
        chkSaturday = (CustomToggleButton) findViewById(R.id.alarm_details_repeat_saturday);
        txtToneSelection = (TextView) findViewById(R.id.alarm_label_tone_selection);


        long id = getIntent().getExtras().getLong("id");

        // IF alarm exists else create new
        if (id == -1) {
            alarmDetails = new AlarmModel();
        } else {
            alarmDetails = dbHelper.getAlarm(id);

            timePicker.setCurrentHour(alarmDetails.timeHour);
            timePicker.setCurrentMinute(alarmDetails.timeMinute);

            edtName.setText(alarmDetails.name);

            chkWeekly.setChecked(alarmDetails.repeatWeekly);
            chkSunday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.SUNDAY));
            chkMonday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.MONDAY));
            chkTuesday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.TUESDAY));
            chkWednesday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.WEDNESDAY));
            chkThursday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.THURSDAY));
            chkFriday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.FRDIAY));
            chkSaturday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.SATURDAY));

            txtToneSelection.setText(RingtoneManager.getRingtone(this, alarmDetails.alarmTone).getTitle(this));

        }

        final LinearLayout ringToneContainer = (LinearLayout) findViewById(R.id.alarm_ringtone_container);
        ringToneContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                startActivityForResult(intent, 1);
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1: {
                    alarmDetails.alarmTone = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

                    TextView txtToneSelection = (TextView) findViewById(R.id.alarm_label_tone_selection);
                    txtToneSelection.setText(RingtoneManager.getRingtone(this, alarmDetails.alarmTone).getTitle(this));
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }
        if(id == R.id.action_save_alarm_details){
            updateModelFromLayout();
            AlarmManagerHelper.cancelAlarms(this);
            AlarmDBHelper dbHelper = new AlarmDBHelper(this);

            if (alarmDetails.id < 0) {
                dbHelper.createAlarm(alarmDetails);
            } else {
                dbHelper.updateAlarm(alarmDetails);
            }
            AlarmManagerHelper.setAlarms(this);

            setResult(RESULT_OK);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    // Get value from layout to put in model
    private void updateModelFromLayout() {

        alarmDetails.timeMinute = timePicker.getCurrentMinute().intValue();
        alarmDetails.timeHour = timePicker.getCurrentHour().intValue();

        alarmDetails.name = edtName.getText().toString();

        alarmDetails.repeatWeekly = chkWeekly.isChecked();

        alarmDetails.setRepeatingDay(AlarmModel.SUNDAY, chkSunday.isChecked());
        alarmDetails.setRepeatingDay(AlarmModel.MONDAY, chkMonday.isChecked());
        alarmDetails.setRepeatingDay(AlarmModel.TUESDAY, chkTuesday.isChecked());
        alarmDetails.setRepeatingDay(AlarmModel.WEDNESDAY, chkWednesday.isChecked());
        alarmDetails.setRepeatingDay(AlarmModel.THURSDAY, chkThursday.isChecked());
        alarmDetails.setRepeatingDay(AlarmModel.FRDIAY, chkFriday.isChecked());
        alarmDetails.setRepeatingDay(AlarmModel.SATURDAY, chkSaturday.isChecked());

        alarmDetails.isEnabled = true;
    }
}
