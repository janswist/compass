package pl.janswist.compass;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.janswist.compass.lib.CompassSensorsActivity;
import pl.janswist.compass.lib.CompassView;

/**
 * @author Jan Świst
 * */
public class MainActivity extends CompassSensorsActivity {

    @Bind(R.id.compassView) CompassView compassView;
    private Location userLocation, originObjectLocation;
    private LocationManager locationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        userLocation = getBestLastKnowLocation(locationManager);
        originObjectLocation = getBestLastKnowLocation(locationManager);

        compassView.initializeCompass(userLocation, originObjectLocation, R.drawable.arrow);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){
            if (requestCode == 77){
                double newLatitude = data.getDoubleExtra("latitude", getBestLastKnowLocation(locationManager).getLatitude());
                double newLongitude = data.getDoubleExtra("longitude", getBestLastKnowLocation(locationManager).getLongitude());
                setNewDestination(newLatitude, newLongitude);
            }
        }
    }

    @OnClick(R.id.btnSetDest)
    void setDestinationClick(){
        Intent i = new Intent(MainActivity.this, DestinationSetter.class);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                MainActivity.this, findViewById(R.id.btnSetDest), "baton");

        ActivityCompat.startActivityForResult(MainActivity.this,
                i, 77, options.toBundle());
    }

    @OnClick(R.id.btnResetDest)
    void resetDestinationClick(){
        setNewDestination(
                getBestLastKnowLocation(locationManager).getLatitude(),
                getBestLastKnowLocation(locationManager).getLongitude());
    }

    private void setNewDestination(double latitude, double longitude) {
        Location objectLocation = new Location("");
        objectLocation.setLatitude(latitude);
        objectLocation.setLongitude(longitude);
        originObjectLocation = objectLocation;

        compassView.initializeCompass(userLocation, originObjectLocation, R.drawable.arrow);
    }

    private Location getBestLastKnowLocation(LocationManager locationManager) {
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null) location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) location = new Location("");
        return location;
    }
}