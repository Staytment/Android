package de.trottl.staytment;

import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import de.trottl.staytment.volley.VolleyController;

public class MainFragment extends Fragment {

    private GoogleMap gMap;
    private Location myLocation;
    private View view;
    private float currentCameraZoomLevel;
    private LatLng cameraLatLang;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        initializeMap();

        return view;
    }

    @Override
    public void onDestroyView() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            try {
                getFragmentManager().beginTransaction().remove(mapFragment).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * function to load map. If map is not created it will create it for you
     */
    private void initializeMap() {
        if (gMap == null) {
            gMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

            // check if map is created successfully or not
            if (gMap == null) {
                Log.e("MAP", "Can't create map");
            }

            gMap.setMyLocationEnabled(true);
            initMapEventHandler();
        }
    }

    private void initMapEventHandler() {
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //TODO implement input for text pictures etc.
                addMarker(latLng, "luQx", "Hello Marker!");
            }
        });

        gMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                currentCameraZoomLevel = cameraPosition.zoom;
                cameraLatLang = cameraPosition.target;

                updateMarker();
            }
        });
    }

    private void updateMarker() {
        getPosts();
    }

    private void renderMarker() {

    }

    private void getPosts() {
        String requestTag = "get_marker";
        String url = "http://api.staytment.com:80/posts?long=%f&lat=%f&distance=%d&filter=point";

        double lat = cameraLatLang.latitude;
        double lon = cameraLatLang.longitude;
        int zoom = (int) currentCameraZoomLevel;

        url = String.format(url, lon, lat, zoom);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//					TODO renderMarker(getDataXYZ(response));

                        Log.d("response", response.toString());
                        Log.d("response", response.toString());
                        Log.d("response", response.toString());
                        Log.d("response", response.toString());
                        Log.d("response", response.toString());
                        Log.d("response", response.toString());
                        Log.d("response", response.toString());
                        Log.d("response", response.toString());
                        Log.d("response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        VolleyController.getInstance().addToRequestQueue(request, requestTag);
    }

    private void centerMyLocation() {
        //TODO need to find best practice to get my current location
//        double myLat = gMap.getMyLocation().getLatitude();
//        double myLng = gMap.getMyLocation().getLongitude();
        moveCameraTo(new LatLng(51.31323248063356, 9.49244499206543));
    }

    //TODO iwo diese ganzen map methoden hin machen damit die nicht in der main rum idlen?

    /**
     * Simple helper method to move the camera to an custom location with an animation
     *
     * @param latLang
     */
    public void moveCameraTo(LatLng latLang) {
        //TODO may adjust the delay or see if we go for some presets(ENUM, statics...) for different actions
        CameraPosition myPosition = CameraPosition.builder().target(new LatLng(latLang.latitude, latLang.longitude)).zoom(12).build();
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
    }

    /**
     * @param latLng
     * @param userName
     * @param message
     */
    public void addMarker(LatLng latLng, String userName, String message) {
        //TODO implement custom marker...
        MarkerOptions marker = new MarkerOptions().position(latLng).title(userName + ": " + message);
        gMap.addMarker(marker);
    }
}
