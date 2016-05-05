package wieniacy.w.kaloszach.poznaj2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.SQLException;

import wieniacy.w.kaloszach.poznaj2.models.ConnectionClass;
import wieniacy.w.kaloszach.poznaj2.models.Friend;

/**
 * Created by Robin on 05.05.2016.
 */
public class DetailsFriendListItemActivity extends AppCompatActivity {
    ProgressDialog dialog;
    Friend CurrentFriend;
    public int USERID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_friend_list_item);
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

                Intent myIntent = new Intent(DetailsFriendListItemActivity.this, FriendsActivity.class);
                DetailsFriendListItemActivity.this.startActivity(myIntent);
            }
            else
            {
            //todo jakies info dodac o niepowodzeniu

            }
        }
    }
}
