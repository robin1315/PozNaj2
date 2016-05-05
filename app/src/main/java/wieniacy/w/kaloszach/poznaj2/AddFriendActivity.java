package wieniacy.w.kaloszach.poznaj2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Layout;
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

/**
 * Created by Robin on 28.04.2016.
 */
public class AddFriendActivity extends AppCompatActivity {

    ArrayList<Friend> AllFriendList;
    ProgressDialog dialog;
    private int USERID;
    ArrayAdapter<Friend> adapter = null;
    Layout thisLauout;
    ListView listView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        AllFriendList = new ArrayList<Friend>();

        USERID =((MyAplicationGlobal) this.getApplication()).getGlobalVarValue();
        listView1 = (ListView) findViewById(R.id.add_friend_list);
        //todo zmienic na wyswietlanie wszytkich z poza listy znajomych
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
                "and UU.ID not like " + USERID);



        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Proszę czekać ...");
        dialog.show();

        lae.execute();


        try {
            ArrayList<Friend> lst = lae.get();
            adapter = new ArrayAdapter<Friend>(this, android.R.layout.simple_list_item_1, lst);
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
                i.putExtra("Friend", false);
                startActivity(i);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_friend, menu);

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
                        "and UU.ID not like " + USERID +
                        " and FULL_NAME like '%"+ as+ "%'" );

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

                        String product = ((TextView) view).getText().toString();

                        // Launching new Activity on selecting single List Item
                        Intent i = new Intent(getApplicationContext(), DetailsFriendListItemActivity.class);
                        // sending data to new activity
                        i.putExtra("Name", AllFriendList.get(position).full_name);
                        i.putExtra("Desc", AllFriendList.get(position).description);
                        i.putExtra("ID", AllFriendList.get(position).id);
                        i.putExtra("Friend", false);
                        startActivity(i);

                    }
                });
                adapter.notifyDataSetChanged();
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

    public class LoadAllUsers extends AsyncTask<Void, Void, ArrayList<Friend>> {

        String query;
        @Override
        protected void onPostExecute(ArrayList<Friend> friends) {
            super.onPostExecute(friends);
            dialog.dismiss();
        }

        public LoadAllUsers(String query) {
            this.query = query;
        }

        @Override
        protected ArrayList<Friend> doInBackground(Void... params) {
            ConnectionClass conn = new ConnectionClass();
            ArrayList<Friend> FriendList = new ArrayList<Friend>();

            try {

                conn.makeQuery(query);

                while(conn.getResult().next()){
                    FriendList.add(new Friend(Integer.parseInt(conn.result.getString("ID")),
                            conn.result.getString("FULL_NAME"),
                            conn.result.getString("DESCRIPTION")
                    ));
                }
                AllFriendList = FriendList;
                Friend[] simpleArray = new Friend[ FriendList.size() ];
                FriendList.toArray(simpleArray);


                return FriendList;

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
            return new ArrayList<Friend>();
        }

    }

}
