package wieniacy.w.kaloszach.poznaj2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
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

public class FriendsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ProgressDialog dialog;
    private int USERID;
    ArrayList<Friend> AllFriendList;
    ArrayList<Friend> AllFriendList2;
    ArrayAdapter<Friend> adapter = null;
    ArrayAdapter<Friend> adapter2 = null;
    LoadAllUsers lae;
    ListView listView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        USERID =((MyAplicationGlobal) this.getApplication()).getGlobalVarValue();
        listView1 = (ListView) findViewById(R.id.friendlistView);
//////////////////////////List View friends

        lae = new LoadAllUsers("SELECT * FROM ( SELECT U2.ID, U2.FULL_NAME, U2.DESCRIPTION FROM USERS U JOIN FRIENDS F ON F.ID_USER=U.ID JOIN USERS U2 ON U2.ID=F.ID_USER2 WHERE F.ID_USER LIKE '" + String.valueOf(USERID) +
                "' UNION ALL SELECT U.ID, U.FULL_NAME, U.DESCRIPTION FROM USERS U JOIN FRIENDS F ON F.ID_USER=U.ID JOIN USERS U2 ON U2.ID=F.ID_USER2 WHERE F.ID_USER2 LIKE '" + String.valueOf(USERID) +
                "' ) RAZEM ORDER BY FULL_NAME", 1);

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Proszę czekać ...");
        dialog.show();

        lae.execute();


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
                Intent i = new Intent(getApplicationContext(), DetailsFriendActivity.class);
                // sending data to new activity
                i.putExtra("Name", AllFriendList.get(position).full_name);
                i.putExtra("Desc", AllFriendList.get(position).description);
                i.putExtra("ID", AllFriendList.get(position).id);
                i.putExtra("Friend", true);
                startActivity(i);

            }
        });
//////////////////////////////////////
        //////////////////////////////////////////List View alll
        lae = new LoadAllUsers("select * " +
                "from USERS UU " +
                        "WHERE UU.FULL_NAME not in " +
                        "( " +
                        " SELECT * " +
                        " FROM " +
                        " ( " +
                        " SELECT U2.FULL_NAME " +
                        " FROM USERS U " +
                        " JOIN FRIENDS F ON F.ID_USER=U.ID " +
                        " JOIN USERS U2 ON U2.ID=F.ID_USER2 " +
                        " WHERE F.ID_USER LIKE "+ USERID +
                        " UNION ALL " +
                        " SELECT U.FULL_NAME " +
                        " FROM USERS U " +
                        " JOIN FRIENDS F ON F.ID_USER=U.ID " +
                        " JOIN USERS U2 ON U2.ID=F.ID_USER2 " +
                        " WHERE F.ID_USER2 LIKE "+ USERID +
                        " ) RAZEM " +
                        " " +
                        ") " +
                        "and UU.ID not like " + USERID, 2);

        listView1 = (ListView) findViewById(R.id.friendlistView2);

        lae.execute();


        try {
            adapter2 = new ArrayAdapter<Friend>(this, android.R.layout.simple_list_item_1, lae.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        listView1.setAdapter(adapter2);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String product = ((TextView) view).getText().toString();


                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getApplicationContext(), DetailsFriendActivity.class);
                // sending data to new activity
                i.putExtra("Name", AllFriendList2.get(position).full_name);
                i.putExtra("Desc", AllFriendList2.get(position).description);
                i.putExtra("ID", AllFriendList2.get(position).id);
                i.putExtra("Friend", false);
                startActivity(i);

            }
        });
        dialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.friend, menu);

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


                //////////////////////////////////////////////////////////All USers
                listView1 = (ListView) findViewById(R.id.friendlistView2);
                LoadAllUsers lae = new LoadAllUsers("select * " +
                        "from USERS UU " +
                        "WHERE UU.FULL_NAME not in " +
                        "( " +
                        " SELECT * " +
                        " FROM " +
                        " ( " +
                        " SELECT U2.FULL_NAME " +
                        " FROM USERS U " +
                        " JOIN FRIENDS F ON F.ID_USER=U.ID " +
                        " JOIN USERS U2 ON U2.ID=F.ID_USER2 " +
                        " WHERE F.ID_USER LIKE " + USERID +
                        " UNION ALL " +
                        " SELECT U.FULL_NAME " +
                        " FROM USERS U " +
                        " JOIN FRIENDS F ON F.ID_USER=U.ID " +
                        " JOIN USERS U2 ON U2.ID=F.ID_USER2 " +
                        " WHERE F.ID_USER2 LIKE " + USERID +
                        " ) RAZEM " +
                        " " +
                        ") " +
                        "and UU.ID not like " + USERID +
                        " and FULL_NAME like '%" + as + "%'", 2);

                lae.execute();

                try {
                    adapter = new ArrayAdapter<Friend>(adapter.getContext(), android.R.layout.simple_list_item_1, lae.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                listView1.setAdapter(adapter);
                listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        //String product = ((TextView) view).getText().toString();

                        // Launching new Activity on selecting single List Item
                        Intent i = new Intent(getApplicationContext(), DetailsFriendActivity.class);
                        // sending data to new activity
                        i.putExtra("Name", AllFriendList2.get(position).full_name);
                        i.putExtra("Desc", AllFriendList2.get(position).description);
                        i.putExtra("ID", AllFriendList2.get(position).id);
                        i.putExtra("Friend", false);
                        startActivity(i);

                    }
                });
                adapter.notifyDataSetChanged();


                //////////////////////////////////////////////  Muy friends
                listView1 = (ListView) findViewById(R.id.friendlistView);
                lae = new LoadAllUsers("SELECT * FROM ( SELECT U2.ID, U2.FULL_NAME, U2.DESCRIPTION FROM USERS U JOIN FRIENDS F ON F.ID_USER=U.ID JOIN USERS U2 ON U2.ID=F.ID_USER2 WHERE F.ID_USER LIKE '" + String.valueOf(USERID) +
                        "' UNION ALL SELECT U.ID, U.FULL_NAME, U.DESCRIPTION FROM USERS U JOIN FRIENDS F ON F.ID_USER=U.ID JOIN USERS U2 ON U2.ID=F.ID_USER2 WHERE F.ID_USER2 LIKE '" + String.valueOf(USERID) +
                        "' ) RAZEM WHERE FULL_NAME like '%" + as + "%' ORDER BY FULL_NAME", 1);

                lae.execute();

                try {
                    adapter2 = new ArrayAdapter<Friend>(adapter2.getContext(), android.R.layout.simple_list_item_1, lae.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                listView1.setAdapter(adapter2);
                listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        //String product = ((TextView) view).getText().toString();

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
                adapter2.notifyDataSetChanged();


                //////////////////////////////////////////////////////////
                dialog.dismiss();
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
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.nav_events) {
            // Handle the camera action
            intent = new Intent(FriendsActivity.this, EventsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {

            intent = new Intent(FriendsActivity.this, GalleryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_friends) {

            intent = new Intent(FriendsActivity.this, FriendsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class LoadAllUsers extends AsyncTask<Void, Void, Friend[]> {

        private String query;
        private int numer;
        public LoadAllUsers(String q, int numer){this.query =q; this.numer = numer; }


        @Override
        protected Friend[] doInBackground(Void... params) {
            ConnectionClass conn = new ConnectionClass();
            ArrayList<Friend> FriendList = new ArrayList<Friend>();

            //String id = (String) USERID.getText().toString();

            try {

                conn.makeQuery(query);

                while(conn.getResult().next()){
                    FriendList.add(new Friend(Integer.parseInt(conn.result.getString("ID")),
                            conn.result.getString("FULL_NAME"),
                            conn.result.getString("DESCRIPTION")
                            ));
                }
                if(numer == 1)
                    AllFriendList = FriendList;
                else if(numer ==2)
                    AllFriendList2 = FriendList;

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
