package wieniacy.w.kaloszach.poznaj2;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import wieniacy.w.kaloszach.poznaj2.models.ConnectionClass;

public class AddEventActivity extends AppCompatActivity {


    EditText mName;
    EditText mDescrition;
    RadioButton mGroupOpen;
    RadioButton mGroupClose;
    EditText mMembers;
    TimePicker mTime;
    DatePicker mDate;
    EditText mDuration;
    AddNewEvent AddNewEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);


        mName = (EditText) findViewById(R.id.add_event_name);
        mDescrition = (EditText) findViewById(R.id.add_event_about);
        mGroupOpen = (RadioButton) findViewById(R.id.add_event_group_open);
        mGroupClose = (RadioButton) findViewById(R.id.add_event_group_close);
        mMembers = (EditText) findViewById(R.id.add_event_member);
        mTime = (TimePicker) findViewById(R.id.timePicker);
        mDate = (DatePicker) findViewById(R.id.datePicker);
        mDuration = (EditText) findViewById(R.id.add_event_duration);

    }


    public void clickAddEvent(View view) {
        ///// napisac walidacje danych


        //if validacja ok
        if(true){

            boolean czy = false;
            if(mGroupClose.isChecked()){
                czy=true;
            }
            int   day  = mDate.getDayOfMonth();
            int   month= mDate.getMonth();
            int   year = mDate.getYear() - 1900;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formatedDate = sdf.format(new Date(year, month, day));

            String formattedTime = "";
            int hour = mTime.getCurrentHour();
            String sHour = "00";
            if(hour < 10){
                sHour = "0"+hour;
            } else {
                sHour = String.valueOf(hour);
            }

            int minute = mTime.getCurrentMinute();
            String sMinute = "00";
            if(minute < 10){
                sMinute = "0"+minute;
            } else {
                sMinute = String.valueOf(minute);
            }

            formattedTime = sHour+":"+sMinute;;


            AddNewEvent = new AddNewEvent((mName.getText().toString()), (mDescrition.getText().toString()), czy, mMembers.getText().toString(), formattedTime ,formatedDate ,mDuration.getText().toString());
            AddNewEvent.execute((Void) null);

        }
    }

    public class AddNewEvent extends AsyncTask<Void, Void, Boolean> {

        private final String mNazwa;
        private final String mDesc;
        private final Boolean mCzy;
        private final String mMembers;
        private final String mTime;
        private final String mDate;
        private final String mDuration;
        Activity mactivity;
        private int Id;

        public AddNewEvent(String name, String desc, Boolean czy, String members, String time, String date , String dur) {
            this.mNazwa = name;
            this.mDesc = desc;
            this.mCzy = czy;
            this.mMembers = members;
            this.mTime = time;
            this.mDate = date;
            this.mDuration = dur;
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            ConnectionClass conn = new ConnectionClass();

            String czy = "Z";
            if(mCzy)
                czy = "O";

            try {
////////////////nowy insert do eventow
                String que = "insert into walenmar_poznaj.EVENT (NAME, DESCRYPTION, TYPE, DURATION_MINUTES, " +
                        "MEMBERS_LIMIT, START_TIME, START_DATE_DATE, CREATION_DATE_DATE, CREATION_TIME, LOC_LEN_T, LOC_WID_T) values ('" + mNazwa
                        + "', '" + mDesc + "', '" +
                        czy + "', '" + mDuration
                        + "', '" + mMembers + "', '" +
                        mTime +  "', '" + mDate +"', curdate(), curtime()"
                        + ", '" + Double.toString(LoginActivity.latitude) + "', '" + Double.toString(LoginActivity.longitude) + "')";

                conn.makeUpdate(que);
                return true;

            } catch (SQLException e) {
                e.getMessage();
                e.printStackTrace();
            } finally {
                try {
                    conn.conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
// zmiana : true -> flase

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {

                Intent myIntent = new Intent(AddEventActivity.this, EventsActivity.class);
                myIntent.putExtra("ID", Id);
                AddEventActivity.this.startActivity(myIntent);

            }
            else
            {


                Snackbar.make(getCurrentFocus(), "Rejestracja się nie powiodła", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent myIntent = new Intent(AddEventActivity.this, EventsActivity.class);
                AddEventActivity.this.startActivity(myIntent);

            }
        }
    }

}