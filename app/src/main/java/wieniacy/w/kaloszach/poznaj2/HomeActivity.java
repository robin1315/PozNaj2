package wieniacy.w.kaloszach.poznaj2;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.SQLException;
import java.util.ArrayList;

import wieniacy.w.kaloszach.poznaj2.models.ConnectionClass;
import wieniacy.w.kaloszach.poznaj2.models.Event;

public class HomeActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener {

    protected static final String TAG = "HomeActivity";
    public int USERID;
    public String USERNAME;
    public String USERSURNAME;
    public String USERLOGIN;
    public String USEREMAIL;
    public String USERDESC;
    public String USERPASS;

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;
    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;
    protected String mLatitudeLabel;
    protected String mLongitudeLabel;
    protected TextView mLatitudeText;
    protected TextView mLongitudeText;

    TextView nameuser;
    TextView emailuser;
    private ProgressDialog dialog;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private GoogleMap mMap;
    public ArrayList<Event> AllEventList;
    public ArrayList<Event> AllEventList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /////////////////////////////////////////////////////////////////////
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

/////////////////////////////////////////////        /////
        Bundle extras = getIntent().getExtras();
        USERID = Integer.parseInt(extras.getString("ID"));
        USEREMAIL = extras.getString("EMAIL");
        USERNAME = extras.getString("NAME");
        USERSURNAME = extras.getString("SURNAME");
        USERLOGIN = extras.getString("LOGIN");
        USERDESC = extras.getString("DESC");
        USERPASS = extras.getString("PASSWORD");


        ((MyAplicationGlobal) this.getApplication()).setGlobalVarValue(USERID);
        ((MyAplicationGlobal) this.getApplication()).setUSEREMAIL(USEREMAIL);
        ((MyAplicationGlobal) this.getApplication()).setUSERNAME(USERNAME);
        ((MyAplicationGlobal) this.getApplication()).setUSERSURNAME(USERSURNAME);
        ((MyAplicationGlobal) this.getApplication()).setUSERLOGIN(USERLOGIN);
        ((MyAplicationGlobal) this.getApplication()).setUSERDESC(USERDESC);
        ((MyAplicationGlobal) this.getApplication()).setUSERPASS(USERPASS);

        LoadAllUsers lae = new LoadAllUsers("Select * from walenmar_poznaj.USERS where ID like  " + USERID, 1);

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Proszę czekać ...");
        dialog.show();

        lae.execute();
        ////////////////////////////////////////////////////Lokalizacja werda

        mLatitudeLabel = getResources().getString(R.string.latitude_label);
        mLongitudeLabel = getResources().getString(R.string.longitude_label);
        mLatitudeText = (TextView) findViewById((R.id.latitude_text));
        mLongitudeText = (TextView) findViewById((R.id.longitude_text));

        buildGoogleApiClient();
        mLatitudeText.setText(Double.toString(LoginActivity.latitude));
        mLongitudeText.setText(Double.toString(LoginActivity.longitude));

        mLatitudeText.setVisibility(View.GONE);
        mLongitudeText.setVisibility(View.GONE);



        LoadAllEvents laee = new LoadAllEvents("Select * from walenmar_poznaj.EVENT ", 2);
        laee.execute();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng mypositon = new LatLng(Double.valueOf(Double.toString(LoginActivity.latitude)), Double.valueOf(Double.toString(LoginActivity.longitude)));
        mMap.addMarker(new MarkerOptions().position(mypositon).title("Tu jestem!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mypositon, 10));

        for (Event e : AllEventList2) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(e.len, e.wid)).title(e.name));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        nameuser = (TextView) findViewById(R.id.nav_user_name);
        emailuser = (TextView) findViewById(R.id.nav_user_email);

        ImageView navImage = (ImageView) findViewById(R.id.imageView);
        nameuser.setText(USERNAME);
        emailuser.setText(USEREMAIL);
////////////////
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.nav_events) {
            // Handle the camera action
            intent = new Intent(HomeActivity.this, EventsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {

            intent = new Intent(HomeActivity.this, GalleryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_friends) {

            intent = new Intent(HomeActivity.this, FriendsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_peoplearound) {
            intent = new Intent(HomeActivity.this, PeopleAroundActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            LoadAllUsers loa = new LoadAllUsers("update walenmar_poznaj.USERS set online = 1  where ID = " + USERID, 0);
            loa.execute();

            intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    /////////////////////////////// Lokalizacja WERDA

    @Override
    protected void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        mGoogleApiClient.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Home Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://wieniacy.w.kaloszach.poznaj2/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Home Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://wieniacy.w.kaloszach.poznaj2/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText.setText(String.format("%s: %f", mLatitudeLabel,
                    mLastLocation.getLatitude()));
            mLongitudeText.setText(String.format("%s: %f", mLongitudeLabel,
                    mLastLocation.getLongitude()));
        } else {
            Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    public class LoadAllUsers extends AsyncTask<Void, Void, Boolean> {

        String query;
        int SELECT = 1;

        public LoadAllUsers(String query, int SELECT) {
            this.query = query;
            this.SELECT = SELECT;
        }

        @Override
        protected void onPostExecute(Boolean friends) {
            super.onPostExecute(friends);
            //todo wymyslec dlaczego to nei dziala
//            nameuser.setText(USERNAME);
//            emailuser.setText(USEREMAIL);
            dialog.dismiss();
        }

        @Override
        protected Boolean doInBackground(Void... parmas) {
            ConnectionClass conn = new ConnectionClass();
            try {

                if (SELECT == 1) {
                    conn.makeQuery(query);

                    while (conn.getResult().next()) {
                        USERNAME = conn.result.getString("FULL_NAME");
                        USEREMAIL = conn.result.getString("EMAIL");

                    }
                } else {
                    conn.makeUpdate(query);
                }

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

    }

    //// Maps events


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

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync((OnMapReadyCallback) mapFragment.getContext());
        }

        @Override
        protected Event[] doInBackground(Void... params) {
            ConnectionClass conn = new ConnectionClass();
            ArrayList<Event> EventList = new ArrayList<Event>();


            try {

                conn.makeQuery(query);
                Event tmp;
                while (conn.getResult().next()) {
                    tmp = new Event(Integer.parseInt(conn.result.getString("ID")),
                            conn.result.getString("NAME"),
                            conn.result.getString("DESCRYPTION"),
                            conn.result.getString("START_DATE_DATE"),
                            conn.result.getString("START_TIME"));

                    tmp.setLen(Double.parseDouble(conn.result.getString("LOC_LEN_T")));
                    tmp.setWid(Double.parseDouble(conn.result.getString("LOC_WID_T")));
                    EventList.add(tmp);
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