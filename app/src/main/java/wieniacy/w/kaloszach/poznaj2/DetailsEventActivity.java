package wieniacy.w.kaloszach.poznaj2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import wieniacy.w.kaloszach.poznaj2.models.ConnectionClass;
import wieniacy.w.kaloszach.poznaj2.models.Event;
import wieniacy.w.kaloszach.poznaj2.models.Friend;

public class DetailsEventActivity extends AppCompatActivity {

    public Event CurrentEvent;
    int USERID;
    ProgressDialog dialog;
    private ArrayList<Friend> AllFriendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_event);
        Bundle extras = getIntent().getExtras();

        CurrentEvent = new Event(extras.getInt("ID"), extras.getString("Name"), extras.getString("Desc"), extras.getString("Date"), extras.getString("Time"));

        USERID = ((MyAplicationGlobal) this.getApplication()).getGlobalVarValue();

        TextView tName = (TextView) findViewById(R.id.event_details_name);
        TextView tDesc = (TextView) findViewById(R.id.event_details_description);
        TextView tDate = (TextView) findViewById(R.id.event_details_date);
        TextView tTime = (TextView) findViewById(R.id.event_details_time);


        Button bJoin = (Button) findViewById(R.id.event_details_button_join);
        Button bLeave = (Button) findViewById(R.id.event_details_button_leave);


        tName.setText(CurrentEvent.name);
        tDesc.setText(CurrentEvent.about);
        tDate.setText(CurrentEvent.date);
        tTime.setText(CurrentEvent.hour);

        ////////// ListView

        ListView listView1 = (ListView) findViewById(R.id.event_details_listView);
        LoadAllMembers lae = new LoadAllMembers();

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Proszę czekać ...");
        dialog.show();

        lae.execute();

        ArrayAdapter<Friend> adapter = null;
        try {
            adapter = new ArrayAdapter<Friend>(this, android.R.layout.simple_list_item_1, lae.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getApplicationContext(), DetailsFriendActivity.class);
                // sending data to new activity
                i.putExtra("Name", AllFriendList.get(position).full_name);
                i.putExtra("Desc", AllFriendList.get(position).description);
                i.putExtra("ID", AllFriendList.get(position).id);
                i.putExtra("Friend", true);
                startActivity(i);

            }
        });
//todo warunek na max member
        if(AllFriendList.size() != 0){
        for (int i = 0; i < AllFriendList.size(); i++) {

            if (AllFriendList.get(i).id == USERID) {

                bJoin.setClickable(false);
                bJoin.setVisibility(View.GONE);
                break;
            } else {
                bLeave.setClickable(false);
                bLeave.setVisibility(View.GONE);
            }
        }}
        else{
            bLeave.setClickable(false);
            bLeave.setVisibility(View.GONE);
        }
        dialog.dismiss();


        /////////////////////

    }

    //////////////////////////ListView
    public class LoadAllMembers extends AsyncTask<Void, Void, Friend[]> {


        @Override
        protected Friend[] doInBackground(Void... params) {
            ConnectionClass conn = new ConnectionClass();
            ArrayList<Friend> FriendList = new ArrayList<Friend>();

            //String id = (String) USERID.getText().toString();

            try {
                //todo zmienic zapytanie na membersy eventu
                conn.makeQuery("SELECT u.* FROM walenmar_poznaj.USERS u " +
                        "join walenmar_poznaj.MEMBERS m on m.ID_USER=u.ID " +
                        "join walenmar_poznaj.EVENT e on e.ID=m.ID_EVENT " +
                        "where e.ID = " + CurrentEvent.id + ";");

                while (conn.getResult().next()) {
                    FriendList.add(new Friend(Integer.parseInt(conn.result.getString("ID")),
                            conn.result.getString("FULL_NAME"),
                            conn.result.getString("DESCRIPTION")
                    ));
                }
                AllFriendList = FriendList;
                Friend[] simpleArray = new Friend[FriendList.size()];
                FriendList.toArray(simpleArray);


                return simpleArray;

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

            //return false;

            return new Friend[0];
        }

    }


    ////////////////////////
    public void OnClickJoin(View view) {

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Proszę czekać ...");
        dialog.show();

        String que = "insert into MEMBERS " +
                "(ID_EVENT,ID_USER) " +
                "values " +
                "(" + CurrentEvent.id + "," +
                USERID + ");";

        DoQueryOnEvent anf = new DoQueryOnEvent(que);
        anf.execute((Void) null);

    }

    public void OnClickLeave(View view) {

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Proszę czekać ...");
        dialog.show();

        String que = "DELETE FROM `walenmar_poznaj`.`MEMBERS`" +
                " WHERE ID_USER = " + USERID +
                " and ID_EVENT = " + CurrentEvent.id + ";";

        DoQueryOnEvent anf = new DoQueryOnEvent(que);
        anf.execute((Void) null);

    }

    public class DoQueryOnEvent extends AsyncTask<Void, Void, Boolean> {

        private String RelType;
        private int nID;
        private String query;

        public DoQueryOnEvent(String q) {
            this.query = q;
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            ConnectionClass conn = new ConnectionClass();

            try {
                conn.makeUpdate(query);
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
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            dialog.dismiss();
            if (success) {

                Intent myIntent = new Intent(DetailsEventActivity.this, EventsActivity.class);
                DetailsEventActivity.this.startActivity(myIntent);
            } else {
                //todo jakies info dodac o niepowodzeniu
                Intent myIntent = new Intent(DetailsEventActivity.this, EventsActivity.class);
                DetailsEventActivity.this.startActivity(myIntent);
            }
        }
    }
}
