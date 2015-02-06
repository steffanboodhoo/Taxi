package main.code.taxi.maps;

import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

import main.code.taxi.taxi.R;
import main.code.taxi.utils.Utils;

/**
 * Created by Steffan on 05/02/2015.
 */
public class MapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    View view;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private String mLastUpdateTime;
    private Location mCurrentLocation;
    private Socket mSocket;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main,container,false);
        MapFragment mapFragment = (MapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(getActivity(), "connected", Toast.LENGTH_SHORT).show();
        createLocationRequest();
        //if (mRequestingLocationUpdates) {
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
        Log.d("Fragment",String.valueOf(mCurrentLocation.getLatitude()));
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
            Toast.makeText(getActivity(),data.toString(),Toast.LENGTH_SHORT).show();
        }
    };
    private Emitter.Listener driverBroadcastRecieve = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject)args[0];
                    Log.d("data",data.toString());
                    Toast.makeText(getActivity(),data.toString(),Toast.LENGTH_SHORT).show();
                }
            });

        }
    };
    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "meh", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

}
