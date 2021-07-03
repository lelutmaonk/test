package com.example.sigwisataprokesbali;

import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String[] nama,alamat,nohp,id;
    int numData;
    LatLng[] latLng;
    Boolean[] markerD;
    private Double[] latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getLokasi(){
        String url = "https://sigdwisatabali.000webhostapp.com/marker.php";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                numData = response.length();
                Log.d("DEBUG_", "Parse JSON");
                latLng = new LatLng[numData];
                markerD = new Boolean[numData];
                nama = new String[numData];
                alamat = new String[numData];
                nohp = new String[numData];
                id = new String[numData];
                latitude = new Double[numData];
                longitude = new Double[numData];

                for (int i = 0; i < numData; i++) {
                    try {
                        JSONObject data = response.getJSONObject(i);
                        id[i] = data.getString("id_wisata");
                        latLng[i] = new LatLng(data.getDouble("latitude"),
                                data.getDouble("longitude"));
                        nama[i] = data.getString("nm_wisata");
                        alamat[i] = data.getString("alamat");
                        nohp[i] = data.getString("no_hp");
                        latitude[i] = data.getDouble("latitude");
                        longitude[i] = data.getDouble("longitude");

                        markerD[i] = false;
                        int height = 100;
                        int width = 100;
                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.icon);
                        Bitmap b = bitmapdraw.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng[i])
                                .title(nama[i])
                                .snippet(alamat[i])
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

                    } catch (JSONException je) {
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng[i], 15.5f));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setTitle("Error!");
                builder.setMessage("No Internet Connection");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getLokasi();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        Volley.newRequestQueue(this).add(request);
    }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLokasi();

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
