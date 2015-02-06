package main.code.taxi.taxi;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

import main.code.taxi.pojo.Passenger;
import main.code.taxi.utils.Utils;

public class Main extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,GoogleMap.OnMapClickListener {

    private HashMap markers,data;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private String mLastUpdateTime;
    private Location mCurrentLocation;
    private Socket mSocket;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        markers= new HashMap();
        data= new HashMap();
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        setupSocket();
        MapFragment myMapFragment = (MapFragment) (getFragmentManager()
                .findFragmentById(R.id.map));
        myMapFragment.getMapAsync(this);
        buildGoogleApiClient();
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        Toast.makeText(this,"optionsItemSelected",Toast.LENGTH_SHORT).show();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //when the activity is destroyed we want to destroy the socket as well
    @Override
    public void onDestroy() {
        super.onDestroy();

        //mSocket.disconnect();
        stopLocationUpdates();
//        mSocket.off("disconnected",onNewMessage);
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Toast.makeText(getApplicationContext(),"message recieved",Toast.LENGTH_SHORT).show();
        }
    };

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        LatLng pos = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));
        createLocationRequest();
        startLocationUpdates();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    private void updateUI() {
        Log.d("Fragment", String.valueOf(mCurrentLocation.getLatitude()));
        //Toast.makeText(getActivity(),""+String.valueOf(mCurrentLocation.getLatitude()),Toast.LENGTH_SHORT).show();
        try{
            JSONObject userData = new JSONObject();
            userData.put("user","steffan");
            userData.put("long",mCurrentLocation.getLongitude());
            userData.put("lat",mCurrentLocation.getLongitude());
            mSocket.emit("traveller-request", userData);
        }catch (Exception e){e.printStackTrace();};

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    private Emitter.Listener travellerBroadcastRecieve= new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject)args[0];
            Toast.makeText(Main.this,data.toString(),Toast.LENGTH_SHORT).show();
        }
    };
    private Emitter.Listener driverBroadcastRecieve = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Log.d("data", data.toString());
                    Toast.makeText(Main.this, data.toString(), Toast.LENGTH_SHORT).show();
                    try {
                        Passenger p = new Passenger(data.getString("identifier"), data.getDouble("lat"), data.getDouble("lng"));
                        Main.this.data.put(data.get("identifier"),p);

                    } catch (Exception e) {e.printStackTrace();}
                }
            });

        }
    };
    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
           runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   Toast.makeText(getApplicationContext(),
                           "meh", Toast.LENGTH_LONG).show();

               }
           });
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleApiClient.connect();
        map=googleMap;
    }
    private void setupSocket(){
        try{
            Log.d("Main", "before socket connection");
            mSocket= IO.socket(Utils.mainUrl);
            mSocket.emit("driver-start","");
            mSocket.on("customer",driverBroadcastRecieve);
            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.connect();
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }
}
