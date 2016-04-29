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
import wieniacy.w.kaloszach.poznaj2.models.Friend;

public class FriendsActivity extends AppCompatActivity {
    ProgressDialog dialog;
    int USERID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

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

    public class LoadAllUsers extends AsyncTask<Void, Void, Friend[]> {


        @Override
        protected Friend[] doInBackground(Void... params) {
            ConnectionClass conn = new ConnectionClass();
            ArrayList<Friend> FriendList = new ArrayList<Friend>();

            //String id = (String) USERID.getText().toString();

            try {

                conn.makeQuery("SELECT * FROM ( SELECT U2.ID, U2.FULL_NAME, U2.DESCRIPTION FROM USERS U JOIN FRIENDS F ON F.ID_USER=U.ID JOIN USERS U2 ON U2.ID=F.ID_USER2 WHERE F.ID_USER LIKE '" + String.valueOf(USERID) +
                        "' UNION ALL SELECT U.ID, U.FULL_NAME, U.DESCRIPTION FROM USERS U JOIN FRIENDS F ON F.ID_USER=U.ID JOIN USERS U2 ON U2.ID=F.ID_USER2 WHERE F.ID_USER2 LIKE '" + String.valueOf(USERID) +
                        "' ) RAZEM ORDER BY FULL_NAME");

                while(conn.getResult().next()){
                    FriendList.add(new Friend(Integer.parseInt(conn.result.getString("ID")),
                            conn.result.getString("FULL_NAME"),
                            conn.result.getString("DESCRIPTION")
                            ));
                }
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
