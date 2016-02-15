package pl.janswist.compass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jan Świst
 * */
public class DestinationSetter extends AppCompatActivity {

    @Bind(R.id.etLatitude) EditText etLat;
    @Bind(R.id.etLongitude) EditText etLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_setter);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnSet)
    void setDestination(){
        int latLength = etLat.getText().length();
        int lonLength = etLon.getText().length();

        if(latLength > 0 && lonLength > 0){

            double latitude = Double.valueOf(etLat.getText().toString());
            double longitude = Double.valueOf(etLon.getText().toString());

            if(-180 <= latitude && latitude <= 180
                    && -90 <= longitude && longitude <= 90){

                Intent returnIntent = new Intent();
                returnIntent.putExtra("latitude", latitude);
                returnIntent.putExtra("longitude", longitude);
                setResult(RESULT_OK, returnIntent);
                finish();

            } else {

                if(latitude > 180 || latitude < -180) etLat.setError(getResources().getString(R.string.wrong_coordinates));
                if(longitude > 90 || longitude < -90) etLon.setError(getResources().getString(R.string.wrong_coordinates));
            }

        } else {
            if(latLength == 0) etLat.setError(getResources().getString(R.string.enter_coordinates));
            if(lonLength == 0) etLon.setError(getResources().getString(R.string.enter_coordinates));
        }
    }
}
