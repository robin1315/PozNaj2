package wieniacy.w.kaloszach.poznaj2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import wieniacy.w.kaloszach.poznaj2.models.ConnectionClass;
import wieniacy.w.kaloszach.poznaj2.models.Friend;

public class PeopleAroundActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ProgressDialog dialog;
    int USERID;
    ArrayList<Friend> AllFriendList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peoplearound);
///////////////////////////////////////////////////////////////////////////////////// navigation bar
        //todo trzeba to jakos zrobic
/*        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/

//////////////////////////////////////////////////////////////////////////////////////////
        USERID =((MyAplicationGlobal) this.getApplication()).getGlobalVarValue();

        ListView listView1 = (ListView) findViewById(R.id.friendlistView);
        LoadAllUsers lae = new LoadAllUsers();

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

                String product = ((TextView) view).getText().toString();

                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getApplicationContext(), DetailsFriendListItemActivity.class);
                // sending data to new activity
                i.putExtra("Name", AllFriendList.get(position).full_name);
                i.putExtra("Desc", AllFriendList.get(position).description);
                i.putExtra("ID", AllFriendList.get(position).id);
                i.putExtra("Friend", true);
                startActivity(i);

            }
        });
        dialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.friend, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.action_add_friend:
                startActivity(new Intent(this, AddFriendActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.nav_events) {
            // Handle the camera action
            intent = new Intent(PeopleAroundActivity.this, EventsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {

            intent = new Intent(PeopleAroundActivity.this, GalleryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_friends) {

            intent = new Intent(PeopleAroundActivity.this, PeopleAroundActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class LoadAllUsers extends AsyncTask<Void, Void, Friend[]> {


        @Override
        protected Friend[] doInBackground(Void... params) {
            ConnectionClass conn = new ConnectionClass();
            ArrayList<Friend> FriendList = new ArrayList<Friend>();

            //String id = (String) USERID.getText().toString();

            try {
                conn.makeQuery("SELECT * FROM walenmar_poznaj.USERS where ABS(LON -'" + LoginActivity.longitude
                        + "') + ABS(LAT - '" + LoginActivity.latitude + "') < " + new Double(LoginActivity.radius / 60) + " AND NOT LOGIN ='" + LoginActivity.name + "'");

                while(conn.getResult().next()){
                    FriendList.add(new Friend(Integer.parseInt(conn.result.getString("ID")),
                            conn.result.getString("FULL_NAME"),
                            conn.result.getString("DESCRIPTION")
                            ));
                }
                AllFriendList = FriendList;
                Friend[] simpleArray = new Friend[ FriendList.size() ];
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


}
