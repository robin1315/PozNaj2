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

/**
 * Created by Robin on 05.05.2016.
 */
public class DetailsFriendActivity extends AppCompatActivity {
    ProgressDialog dialog;
    Friend CurrentFriend;
    public int USERID;
    ArrayList<Event> AllEventList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_friend);
        Bundle extras = getIntent().getExtras();
        CurrentFriend = new Friend(extras.getInt("ID"), extras.getString("Name"), extras.getString("Desc"));

        USERID =((MyAplicationGlobal) this.getApplication()).getGlobalVarValue();

        TextView tName = (TextView) findViewById(R.id.friend_list_item_name);
        TextView tDesc = (TextView) findViewById(R.id.friend_list_item_description);
        Button bDodaj = (Button) findViewById(R.id.friend_list_button_add);
        Button bUsun = (Button) findViewById(R.id.friend_list_button_remove);


        if(extras.getBoolean("Friend")){

            bDodaj.setClickable(false);
            bDodaj.setVisibility(View.GONE);
        }
        else {

            bUsun.setClickable(false);
            bUsun.setVisibility(View.GONE);
        }

        tName.setText(CurrentFriend.full_name);
        tDesc.setText(CurrentFriend.description);

/////////////////////////////////////////////////////////////////////////////////////////EVEnt
        ListView listView1 = (ListView) findViewById(R.id.friend_details_listView);
        LoadAllEvents lae = new LoadAllEvents();

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Proszę czekać ...");
        dialog.show();

        lae.execute();

        ArrayAdapter<Event> adapter = null;
        try {
            adapter = new ArrayAdapter<Event>(this, android.R.layout.simple_list_item_1, lae.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String product = ((TextView) view).getText().toString();

                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getApplicationContext(), DetailsEventActivity.class);
                // sending data to new activity
                i.putExtra("Name", AllEventList.get(position).name);
                i.putExtra("Desc", AllEventList.get(position).about);
                i.putExtra("ID", AllEventList.get(position).id);
                i.putExtra("Date", AllEventList.get(position).date);
                i.putExtra("Time", AllEventList.get(position).hour);
                i.putExtra("Place", AllEventList.get(position).place); // is nulll
                i.putExtra("Event", true);
                startActivity(i);

            }
        });


        //////////////////

    }
    public void OnClickDodaj(View view){
//todo dodatkowe pole typu np radio buttn do relacji
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Proszę czekać ...");
        dialog.show();

        String que = "INSERT INTO `walenmar_poznaj`.`FRIENDS` " +
                "(`ID_USER`," +
                "`ID_USER2`," +
                "`REL_TYPE`) " +
                "VALUES" +
                "(" + USERID + "," +
                CurrentFriend.id + "," +
                "'FRI');";

        DoQueryOnFriend  anf = new DoQueryOnFriend(que);
        anf.execute((Void) null);

    }

    public void OnClickUsun(View view){
//todo dodatkowe pole typu np radio buttn do relacji
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Proszę czekać ...");
        dialog.show();

        String que = "DELETE FROM `walenmar_poznaj`.`FRIENDS`" +
                " WHERE ID_USER = "+ USERID  +
                " and ID_USER2 = "+ CurrentFriend.id + ";";

        DoQueryOnFriend  anf = new DoQueryOnFriend(que);
        anf.execute((Void) null);

    }
    public class DoQueryOnFriend extends AsyncTask<Void, Void, Boolean> {

        private String RelType;
        private int nID;
        private String query;
        public DoQueryOnFriend(String q) {
            this.query = q;
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            ConnectionClass conn = new ConnectionClass();

///Todo  sprawdzic czy dziala
            try {
//                String que = "INSERT INTO `walenmar_poznaj`.`FRIENDS`" +
//                        "(`ID_USER`," +
//                        "`ID_USER2`," +
//                        "`REL_TYPE`)" +
//                        "VALUES" +
//                        "(" + USERID + "," +
//                        nID + "," +
//                        "'" + RelType + "');";

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

                Intent myIntent = new Intent(DetailsFriendActivity.this, FriendsActivity.class);
                DetailsFriendActivity.this.startActivity(myIntent);
            }
            else
            {
            //todo jakies info dodac o niepowodzeniu

            }
        }
    }


    //////////////Event
    public class LoadAllEvents extends AsyncTask<Void, Void, Event[]> {

        @Override
        protected void onPostExecute(Event[] Events) {
            super.onPostExecute(Events);
            dialog.dismiss();
        }

        @Override
        protected Event[] doInBackground(Void... params) {
            ConnectionClass conn = new ConnectionClass();
            ArrayList<Event> EventList = new ArrayList<Event>();



            try {

                conn.makeQuery("Select * from walenmar_poznaj.EVENT e " +
                        "JOIN MEMBERS m on e.ID=m.ID_EVENT " +
                        "WHERE ID_USER like " + CurrentFriend.id);

                while(conn.getResult().next()){
                    EventList.add(new Event(Integer.parseInt(conn.result.getString("ID")),
                            conn.result.getString("NAME"),
                            conn.result.getString("DESCRYPTION"),
                            conn.result.getString("START_DATE_DATE"),
                            conn.result.getString("START_TIME")));
                }
                AllEventList = EventList;
                Event[] simpleArray = new Event[ EventList.size() ];
                EventList.toArray(simpleArray);


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

            return new Event[0];
        }

    }
}
