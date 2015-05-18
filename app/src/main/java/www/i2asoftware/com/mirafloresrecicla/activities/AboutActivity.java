package www.i2asoftware.com.mirafloresrecicla.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnClick;
import www.i2asoftware.com.mirafloresrecicla.R;

public class AboutActivity extends FragmentActivity {

    private GoogleMap mMap;

  @Override protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_about);
      ButterKnife.inject(this);

//    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      SharedPreferences prefs = getSharedPreferences("DB", MODE_PRIVATE);
      String restoredText = prefs.getString("name", null);
      if (restoredText != null) {
          ((Button) findViewById(R.id.btn_faq)).setText("Nombre: " + prefs.getString("name", "Nombre"));//"No name defined" is the default value.
      }
      String restoredText2 = prefs.getString("disp", null);
      if (restoredText2 != null) {
          ((Button) findViewById(R.id.btn_feedback)).setText("Disponibilidad: " + prefs.getString("disp", "Horario Disponible"));
      }
      String restoredText3 = prefs.getString("telf", null);
      if (restoredText3 != null) {
              ((Button) findViewById(R.id.btn_contact)).setText("Contacto: " + prefs.getString("telf", "Contacto"));
      }

      findViewById(R.id.btn_faq).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(final View v) {
              AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);
              builder.setTitle("Actualizar informacion");
              //builder.setMessage(R.string.message_alertDialog);
              LayoutInflater inflater = AboutActivity.this.getLayoutInflater();
              final EditText input = (EditText) inflater.inflate(R.layout.textview, null);
              builder.setView(input);
              input.getNextFocusDownId();

              builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int whichButton) {
                      ((Button) v).setText("Nombre: " + input.getText().toString());
                      SharedPreferences.Editor editor = getSharedPreferences("DB", MODE_PRIVATE).edit();
                      editor.putString("name", input.getText().toString());
                      editor.commit();
                  }
              });

              builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int whichButton) {
                  }
              });

              AlertDialog dialog = builder.create();
              dialog.setCanceledOnTouchOutside(true);
              dialog.show();
          }
      });

      findViewById(R.id.btn_feedback).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(final View v) {
              AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);
              builder.setTitle("Actualizar informacion");
              //builder.setMessage(R.string.message_alertDialog);
              LayoutInflater inflater = AboutActivity.this.getLayoutInflater();
              final EditText input = (EditText) inflater.inflate(R.layout.textview, null);
              builder.setView(input);
              input.getNextFocusDownId();

              builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int whichButton) {
                      ((Button) v).setText("Disponibilidad: " + input.getText().toString());
                      SharedPreferences.Editor editor = getSharedPreferences("DB", MODE_PRIVATE).edit();
                      editor.putString("disp", input.getText().toString());
                      editor.commit();
                  }
              });

              builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int whichButton) {
                  }
              });

              AlertDialog dialog = builder.create();
              dialog.setCanceledOnTouchOutside(true);
              dialog.show();
          }
      });

      findViewById(R.id.btn_contact).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(final View v) {
              AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);
              builder.setTitle("Actualizar informacion");
              //builder.setMessage(R.string.message_alertDialog);
              LayoutInflater inflater = AboutActivity.this.getLayoutInflater();
              final EditText input = (EditText) inflater.inflate(R.layout.textview, null);
              builder.setView(input);
              input.getNextFocusDownId();

              builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int whichButton) {
                      ((Button) v).setText("Contacto: " + input.getText().toString());
                      SharedPreferences.Editor editor = getSharedPreferences("DB", MODE_PRIVATE).edit();
                      editor.putString("telf", input.getText().toString());
                      editor.commit();
                  }
              });

              builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int whichButton) {
                  }
              });

              AlertDialog dialog = builder.create();
              dialog.setCanceledOnTouchOutside(true);
              dialog.show();
          }
      });
  }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    public void setUpMapIfNeeded() {
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                mMap.getUiSettings().setAllGesturesEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.getUiSettings().setCompassEnabled(false);
                mMap.setMyLocationEnabled(false);

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(-12.12169908, -77.02915169))
                        .zoom(18)
                        .bearing(0)
                        .tilt(30)
                        .build();

                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                // Check if we were successful in obtaining the map.
                if (mMap != null) {
//                mMap.addMarker(new MarkerOptions()

                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    Address address = null;
                    try {
                        address = geocoder.getFromLocation(-12.12169908, -77.02915169, 1).get(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mMap.addMarker(new MarkerOptions().position(new LatLng(-12.12169908, -77.02915169))
                            .title(address.getAddressLine(0) + ", " + address.getLocality())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                }
            }
        }
    }

}
