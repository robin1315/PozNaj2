package wieniacy.w.kaloszach.poznaj2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import wieniacy.w.kaloszach.poznaj2.models.Friend;

public class FriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        ListView listView1 = (ListView) findViewById(R.id.friendlistView);

        //wczytywane z bazy
        Friend[] items = {
                new Friend(1, "Robert", "Kowalski"),
                new Friend(2, "Michał", "Nowak"),
                new Friend(3, "Iwona", "Yogurt"),
                new Friend(4, "Jarek", "Costam"),
        };

        ArrayAdapter<Friend> adapter = new ArrayAdapter<Friend>(this,
                android.R.layout.simple_list_item_1, items);

        listView1.setAdapter(adapter);
    }
}
