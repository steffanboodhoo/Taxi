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

import com.getbase.floatingactionbutton.FloatingActionButton;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

import main.code.taxi.help.ClickManager;
import main.code.taxi.maps.PopupRequestService;
import main.code.taxi.pojo.Driver;
import main.code.taxi.pojo.Passenger;
import main.code.taxi.utils.PreferenceManager;
import main.code.taxi.utils.Utils;

public class Main extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
        ,GoogleMap.OnMapClickListener,GoogleMap.OnMarkerClickListener,GoogleMap.OnMarkerDragListener {

    public boolean markReady=false;
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
    private Utils.UserType user;
    private Marker currMark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //store the type of user it is
        if(PreferenceManager.getUserType(this).equals(Utils.userTypeDriver))
            user= Utils.UserType.DriverType;
        else
            user= Utils.UserType.PassengerType;

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
        setup();
        MapFragment myMapFragment = (MapFragment) (getFragmentManager()
                .findFragmentById(R.id.map));
        myMapFragment.getMapAsync(this);
        buildGoogleApiClient();
    }

    private void setup() {
        FloatingActionButton b1 = (FloatingActionButton)findViewById(R.id.action_a);
        FloatingActionButton b2 = (FloatingActionButton)findViewById(R.id.action_b);
        FloatingActionButton b3 = (FloatingActionButton)findViewById(R.id.action_c);
        ClickManager c = new ClickManager(this);
        b1.setOnClickListener(c);
        b2.setOnClickListener(c);
        b3.setOnClickListener(c);
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
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()), 19));
        if(user== Utils.UserType.PassengerType)
            map.addMarker(new MarkerOptions().position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("you"));
        else
            map.addMarker(new MarkerOptions().position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("you"));
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
        //Toast.makeText(getActivity(),""+String.valueOf(mCurrentLocation.getLatitude()),Toast.LENGTH_SHORT).show();
        try{
            JSONObject userData = new JSONObject();
            userData.put("user",PreferenceManager.getUserName(this));
            userData.put(Utils.json_key_lng,mCurrentLocation.getLongitude());
            userData.put(Utils.json_key_lat,mCurrentLocation.getLatitude());
            if(user==Utils.UserType.DriverType) {
                userData.put(Utils.json_key_plate,PreferenceManager.getUserName(this));
                mSocket.emit("driver-broadcast", userData);//sends to driver info
            }else{
                mSocket.emit("user-request", userData);
            }

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

        }
    };
    private Emitter.Listener BroadcastRecieve = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    handleBroadcast((JSONObject) args[0]);
                }
            });
        }
    };
    public void handleBroadcast(JSONObject data){
        try {
            LatLng pos = new LatLng(data.getDouble(Utils.json_key_lat),data.getDouble(Utils.json_key_lng));
            Passenger p = new Passenger(data.getString(Utils.json_key_identifier), data.getDouble(Utils.json_key_lat), data.getDouble(Utils.json_key_lng));
            Main.this.data.put(data.get(Utils.json_key_identifier),p);
            Marker m=(Marker)Main.this.markers.get(data.get(Utils.json_key_identifier));
            if(m==null) {
                if(user== Utils.UserType.DriverType) {
                    m = map.addMarker(new MarkerOptions().title(data.getString(Utils.json_key_identifier)).position(pos));//todo
                }else {
                    m = map.addMarker(new MarkerOptions().title(data.getString(Utils.json_key_identifier)).position(pos)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_driver)));
                }
                markers.put(data.getString(Utils.json_key_identifier),m);
            }else
                m.setPosition(pos);
        } catch (Exception e) {e.printStackTrace();}
    }
    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
           runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   Toast.makeText(getApplicationContext(),
                           "Connection Error, Try again later", Toast.LENGTH_LONG).show();

               }
           });
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleApiClient.connect();
        map=googleMap;
        map.setOnMarkerClickListener(this);
        map.setOnMapClickListener(this);
        map.setOnMarkerDragListener(this);
    }
    private void setupSocket(){
        try{
            Log.d("Main", "before socket connection");
            mSocket= IO.socket(Utils.mainUrl);
            if(user==Utils.UserType.DriverType) {
                mSocket.emit("driver-start", "");
                mSocket.on("see-user", BroadcastRecieve);
            }else{
                mSocket.emit("user-start", "");
                mSocket.on("driver-info", BroadcastRecieve);
            }
            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.connect();
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(markReady){
            if(currMark==null)
                currMark=map.addMarker(new MarkerOptions().draggable(true).position(latLng));
            else
                currMark.setPosition(latLng);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String title=(String)data.get(marker.getTitle());
        Bundle b=new Bundle();
        if(user== Utils.UserType.DriverType){
            PopupRequestService p = new PopupRequestService();
            Passenger passenger=(Passenger)data.get(title);
            if(passenger!=null)
                return false;
            b.putString(Utils.json_key_identifier,passenger.getIdentifier());
            p.setArguments(b);
            p.show(getFragmentManager(),"fm");
        }else{
            PopupRequestService p = new PopupRequestService();
            Driver driver=(Driver)data.get(title);
            if(driver==null)
                    return false;
            b.putString(Utils.json_key_identifier,driver.getIdentifier());
            p.setArguments(b);
            p.show(getFragmentManager(), "fm");
        }
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }
}
