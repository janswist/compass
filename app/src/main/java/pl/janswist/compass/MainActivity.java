package pl.janswist.compass;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.Button;

import pl.janswist.compass.lib.CompassSensorsActivity;
import pl.janswist.compass.lib.CompassView;

/**
 * @author Jan Świst
 * */
public class MainActivity extends CompassSensorsActivity {

    private CompassView compassView;
    private Location userLocation, originObjectLocation;
    private LocationManager locationManager;
    private Button btnSetDestination, btnResetDestination;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        userLocation = getBestLastKnowLocation(locationManager);
        originObjectLocation = getBestLastKnowLocation(locationManager);

        compassView = (CompassView) findViewById(R.id.compassView);
        compassView.initializeCompass(userLocation, originObjectLocation, R.drawable.arrow);

        btnSetDestination = (Button) findViewById(R.id.btnSetDest);
        btnSetDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, DestinationSetter.class);

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        MainActivity.this, findViewById(R.id.btnSetDest), "baton");

                ActivityCompat.startActivityForResult(MainActivity.this,
                        i, 77, options.toBundle());
            }
        });

        btnResetDestination = (Button) findViewById(R.id.btnResetDest);
        btnResetDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setNewDestination(
                        getBestLastKnowLocation(locationManager).getLatitude(),
                        getBestLastKnowLocation(locationManager).getLongitude());
            }
        });
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