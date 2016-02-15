package pl.janswist.compass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author Jan Świst
 * */
public class DestinationSetter extends AppCompatActivity {

    private EditText etLat, etLon;
    private Button btnSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_setter);

        etLat = (EditText) findViewById(R.id.etLatitude);
        etLon = (EditText) findViewById(R.id.etLongitude);

        btnSet = (Button) findViewById(R.id.btnSet);
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        });
    }
}
