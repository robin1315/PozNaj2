package wieniacy.w.kaloszach.poznaj2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import wieniacy.w.kaloszach.poznaj2.models.Event;

public class EventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        ListView listView1 = (ListView) findViewById(R.id.eventlistView);

        //wczytywane z bazy
        Event[] items = {
                new Event(1, "Rozmowa przy kawie", "10", "Kwiecień", "11:00","Mokka", "Szukam miłej osoby na spotkanie"),
                new Event(2, "Potańczyć", "12", "kwiecien", "21:00", "Czekolada", "Chetnie spotkam osoby które potrafią się bawić"),
        };
        ArrayAdapter<Event> adapter = new ArrayAdapter<Event>(this,
                android.R.layout.simple_list_item_1, items);

        listView1.setAdapter(adapter);
    }
}
