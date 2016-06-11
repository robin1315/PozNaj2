package wieniacy.w.kaloszach.poznaj2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import wieniacy.w.kaloszach.poznaj2.models.ConnectionClass;
import wieniacy.w.kaloszach.poznaj2.models.Event;

public class EventsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ProgressDialog dialog;
    ListView listView1;
    ArrayList<Event> AllEventList;
    ArrayList<Event> AllEventList2;
    ArrayAdapter<Event> adapter = null;
    ArrayAdapter<Event> adapter2 = null;
    private int USERID;
    LoadAllEvents lae;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        USERID = ((MyAplicationGlobal) this.getApplication()).getGlobalVarValue();

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Proszę czekać ...");
        dialog.show();

//////////////////////////////////////////////////////////////    My event

        listView1 = (ListView) findViewById(R.id.eventlistView);
        lae = new LoadAllEvents("Select * from walenmar_poznaj.EVENT e " +
                "JOIN MEMBERS m on e.ID=m.ID_EVENT " +
                "WHERE ID_USER like " + USERID, 1);
        lae.execute();
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
        ////////////////////////////////
        ////////////////////////All Event

        listView1 = (ListView) findViewById(R.id.eventlistView2);
        lae = new LoadAllEvents("Select * from walenmar_poznaj.EVENT ", 2);
        lae.execute();
        try {
            adapter2 = new ArrayAdapter<Event>(this, android.R.layout.simple_list_item_1, lae.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        listView1.setAdapter(adapter2);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getApplicationContext(), DetailsEventActivity.class);
                // sending data to new activity
                i.putExtra("Name", AllEventList2.get(position).name);
                i.putExtra("Desc", AllEventList2.get(position).about);
                i.putExtra("ID", AllEventList2.get(position).id);
                i.putExtra("Date", AllEventList2.get(position).date);
                i.putExtra("Time", AllEventList2.get(position).hour);
                i.putExtra("Place", AllEventList2.get(position).place); // is nulll
                i.putExtra("Event", true);
                startActivity(i);
            }
        });

        /////////////////////////////////////////
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                dialog = new ProgressDialog(dialog.getContext());
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.setMessage("Proszę czekać ...");
                dialog.show();

                String as = searchView.getQuery().toString();
//todo zmienic zapytanie wszyscy z wyjatkiem przyjaciol
                ////////////////////////All Event

                listView1 = (ListView) findViewById(R.id.eventlistView2);
                lae = new LoadAllEvents("Select * from walenmar_poznaj.EVENT " + " where NAME like '%" + as + "%'", 2);
                lae.execute();
                try {
                    adapter2 = new ArrayAdapter<Event>(adapter2.getContext(), android.R.layout.simple_list_item_1, lae.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                listView1.setAdapter(adapter2);
                listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Launching new Activity on selecting single List Item
                        Intent i = new Intent(getApplicationContext(), DetailsEventActivity.class);
                        // sending data to new activity
                        i.putExtra("Name", AllEventList2.get(position).name);
                        i.putExtra("Desc", AllEventList2.get(position).about);
                        i.putExtra("ID", AllEventList2.get(position).id);
                        i.putExtra("Date", AllEventList2.get(position).date);
                        i.putExtra("Time", AllEventList2.get(position).hour);
                        i.putExtra("Place", AllEventList2.get(position).place); // is nulll
                        i.putExtra("Event", true);
                        startActivity(i);
                    }
                });

                /////////////////////////////////////////

//////////////////////////////////////////////////////////////    My event

                listView1 = (ListView) findViewById(R.id.eventlistView);
                lae = new LoadAllEvents("Select * from walenmar_poznaj.EVENT e " +
                        "JOIN MEMBERS m on e.ID=m.ID_EVENT " +
                        "WHERE ID_USER like " + USERID +" and e.NAME like '%" + as + "%'", 1);
                lae.execute();
                try {
                    adapter = new ArrayAdapter<Event>(adapter.getContext(), android.R.layout.simple_list_item_1, lae.get());
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
                ////////////////////////////////


                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add_event:
                startActivity(new Intent(this, AddEventActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    public class LoadAllEvents extends AsyncTask<Void, Void, Event[]> {

        private String query;
        private int numer;

        public LoadAllEvents(String qu, int num) {
            this.query = qu;
            this.numer = num;
        }

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

                conn.makeQuery(query);

                while (conn.getResult().next()) {
                    EventList.add(new Event(Integer.parseInt(conn.result.getString("ID")),
                            conn.result.getString("NAME"),
                            conn.result.getString("DESCRYPTION"),
                            conn.result.getString("START_DATE_DATE"),
                            conn.result.getString("START_TIME")));
                }
                if (numer == 1)
                    AllEventList = EventList;
                else if (numer == 2)
                    AllEventList2 = EventList;

                Event[] simpleArray = new Event[EventList.size()];
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
