package de.trottl.staytment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cocoahero.android.geojson.Feature;
import com.cocoahero.android.geojson.FeatureCollection;
import com.cocoahero.android.geojson.GeoJSON;
import com.cocoahero.android.geojson.Position;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.trottl.staytment.volley.VolleyController;

public class MainFragment extends Fragment {

    SharedPreferences shPref_settings, shPref;
    List<postMarker> listMarker;
    HashMap<postMarker, Boolean> markerList;
    private GoogleMap gMap;
    private Location myLocation;
    private View view;
    private float currentCameraZoomLevel;
    private LatLng cameraLatLang;
    private Button addMarkerBtn, openNavDrawBtn;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        initializeMap();

        shPref_settings = getActivity().getSharedPreferences("Staytment_Settings", Context.MODE_PRIVATE);
        shPref = getActivity().getSharedPreferences("Staytment", Context.MODE_PRIVATE);
        listMarker = new ArrayList<postMarker>();
        markerList = new HashMap<postMarker, Boolean>();

        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) getActivity().findViewById(R.id.left_drawer);

        addUIHandler();

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
            } else {
                gMap.setMyLocationEnabled(true);
                initMapEventHandler();
            }
        }
    }

    private void initMapEventHandler() {
        /*
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (shPref.getString("Email", null) != null) {
                    addMarker(latLng, "luQx", "Hello Marker!");
                }
            }
        });
        */
        gMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                cameraLatLang = cameraPosition.target;

                updateMarker();
            }
        });
    }

    private void updateMarker() {
        getPosts();
    }

    private void renderMarker() {
        if (gMap != null) {
            for (postMarker pMarker : listMarker) {
                if (!pMarker.isShown()) {
                    gMap.addMarker(new MarkerOptions().title(pMarker.getName()).snippet(pMarker.getMessage()).position(new LatLng(pMarker.getLatitude(), pMarker.getLongitude())));
                    pMarker.setShown(true);
                }
            }
        }
    }

    private void getPosts() {
        String requestTag = "get_marker";
        String url = "http://api.staytment.com:80/posts?long=%f&lat=%f&distance=%d&filter=point";

        double lat = cameraLatLang.latitude;
        final double lon = cameraLatLang.longitude;
        int distance = shPref_settings.getInt("map_load_distance", 5000);

        //Log.i("Staytment_MAP", "Distance: " + String.valueOf(distance) + "meters");

        url = String.format(Locale.US, url, lon, lat, distance);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseResult(response);
                        renderMarker();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        VolleyController.getInstance().addToRequestQueue(request, requestTag);
    }

    private boolean parseResult(JSONObject response) {
        try {
            FeatureCollection featureCollection = (FeatureCollection) GeoJSON.parse(response.toString());
            List<Feature> list_feature = featureCollection.getFeatures();
            com.cocoahero.android.geojson.Point point;
            Position position;
            JSONObject properties;
            String username, message, messageID;
            postMarker pMarker;

            for (Feature feature : list_feature) {
                point = (com.cocoahero.android.geojson.Point) feature.getGeometry();
                position = point.getPosition();
                properties = feature.getProperties();
                username = properties.getJSONObject("user").optString("name");
                message = properties.optString("message");
                messageID = feature.getIdentifier();

                pMarker = new postMarker(position.getLongitude(),
                        position.getLatitude(),
                        message,
                        username,
                        messageID);
                pMarker.setShown(false);

                if (!listMarker.contains(pMarker)) {
                    listMarker.add(pMarker);
                }
            }
            //Log.i("Staytment_MAP", "Count marker: " + listMarker.size());
            return true;
        } catch (Exception e) {
            Log.e("Staytment_Error", e.toString());
            return false;
        }
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
    public void addMarker(final LatLng latLng, final String userName, final String message) {
        final String TAG = "post_marker";
        String apiKey = shPref.getString("Apikey", null);
        String url = "http://api.staytment.com:80/posts/?api_key=%s";
        if (apiKey == null)
            return;

        url = String.format(url, apiKey);
        Map<String, Object> params = new HashMap<>();

        double[] coords = new double[]{latLng.longitude, latLng.latitude};
        params.put("coordinates", (Object) coords);
        params.put("message", message);
        JSONObject jsonObject = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                gMap.addMarker(new MarkerOptions().title(userName).snippet(message).position(latLng));
                                getActivity().findViewById(R.id.overlay_add_marker).setVisibility(View.GONE);
                                CustomToast cToast = new CustomToast(getActivity(), getString(R.string.add_marker_post_successful));
                                cToast.show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d("exception:", error.getMessage());
                                CustomToast cToast = new CustomToast(getActivity(), getString(R.string.add_marker_post_error));
                                cToast.show();
                            }
                        }) {
                };

        VolleyController.getInstance().addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void addUIHandler() {

        addMarkerBtn = (Button) view.findViewById(R.id.marker_add);
        if (addMarkerBtn != null) {

            final TextView txt_longitude = (TextView) view.findViewById(R.id.add_marker_longitude_txt);
            final TextView txt_latitude = (TextView) view.findViewById(R.id.add_marker_latitude_txt);
            final TextView txt_username = (TextView) view.findViewById(R.id.add_marker_username_txt);
            final EditText txt_message = (EditText) view.findViewById(R.id.add_marker_text_edit);

            addMarkerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getActivity().findViewById(R.id.overlay_add_marker).getVisibility() == View.GONE) {
                        if (txt_username != null) {
                            txt_username.setText(String.format(getString(R.string.add_marker_username_txt), shPref.getString("Name", null)));
                        }
                        if (txt_latitude != null && gMap != null) {
                            txt_latitude.setText(String.format(getString(R.string.add_marker_latitude_txt), gMap.getMyLocation().getLatitude()));
                        }
                        if (txt_longitude != null && gMap != null) {
                            txt_longitude.setText(String.format(getString(R.string.add_marker_longitude_txt), gMap.getMyLocation().getLongitude()));
                        }
                        getActivity().findViewById(R.id.overlay_add_marker).setVisibility(View.VISIBLE);
                    } else {
                        getActivity().findViewById(R.id.overlay_add_marker).setVisibility(View.GONE);
                    }
                }
            });

            Button btn_addMarkerSubmit = (Button) view.findViewById(R.id.btn_marker_submit);
            Button btn_abortMarkerSubmit = (Button) view.findViewById(R.id.btn_marker_abort);


            if (btn_addMarkerSubmit != null) {
                btn_addMarkerSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LatLng latLng = new LatLng(gMap.getMyLocation().getLatitude(), gMap.getMyLocation().getLongitude());
                        String uName = shPref.getString("Name", null);
                        if (gMap.getMyLocation() != null && uName != null && txt_message != null) {
                            addMarker(latLng, uName, txt_message.getText().toString());
                        } else {
                            CustomToast cToast = new CustomToast(getActivity(), "Error while posting");
                            cToast.show();
                        }
                    }
                });
            }

            if (btn_abortMarkerSubmit != null) {
                btn_abortMarkerSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().findViewById(R.id.overlay_add_marker).setVisibility(View.GONE);
                    }
                });
            }
        }

        openNavDrawBtn = (Button) view.findViewById(R.id.nav_drawer_open);
        if (openNavDrawBtn != null) {
            openNavDrawBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDrawerLayout.openDrawer(mDrawerList);
                }
            });
        }


    }
}
