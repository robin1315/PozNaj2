package wieniacy.w.kaloszach.poznaj2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import wieniacy.w.kaloszach.poznaj2.models.ConnectionClass;
import wieniacy.w.kaloszach.poznaj2.models.Event;

public class EventsActivity extends AppCompatActivity {

    ProgressDialog dialog;
    ListView listView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);


        listView1 = (ListView) findViewById(R.id.eventlistView);
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.action_add_event:
                startActivity(new Intent(this, AddEventActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

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

                conn.makeQuery("Select * from walenmar_poznaj.EVENT ");

                while(conn.getResult().next()){
                    EventList.add(new Event(Integer.parseInt(conn.result.getString("ID")),
                            conn.result.getString("NAME"),
                            conn.result.getString("DESCRYPTION"),
                            conn.result.getString("START_DATE_DATE"),
                            conn.result.getString("START_TIME")));
                }
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
