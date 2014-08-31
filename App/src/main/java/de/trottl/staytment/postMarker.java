package de.trottl.staytment;

import com.google.android.gms.maps.model.LatLng;

public class postMarker {

    private double longitude, latitude;
    private String message, name, id;
    private LatLng latLng;

    private boolean isShown;

    public postMarker(double longitude, double latitude, String message, String name, String id) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.message = message;
        this.name = name;
        this.id = id;
    }

    /*
        public postMarker(JSONObject jsonObject) {
            FeatureCollection featureCollection = (FeatureCollection) GeoJSON.parse(jsonObject);
            List<Feature> list_feature = featureCollection.getFeatures();
            com.cocoahero.android.geojson.Point point;
            Position position;
        }
    */
    public postMarker(LatLng latLng, String message, String name, String id) {
        this.latLng = latLng;
        this.message = message;
        this.name = name;
        this.id = id;
    }

    public postMarker() {
    }

    @Override
    public boolean equals(Object o) {

        boolean bool = false;

        if (o != null && o instanceof postMarker) {
            bool = ((postMarker) o).getId().equals(this.getId());
        }

        return bool;
        //return super.equals(o);
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
        this.longitude = latLng.longitude;
        this.latitude = latLng.latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean isShown) {
        this.isShown = isShown;
    }
}
