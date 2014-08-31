package com.cocoahero.android.geojson;

import android.os.Parcel;
import android.os.Parcelable;

import com.cocoahero.android.geojson.util.JSONUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class Feature extends GeoJSONObject {

    // ------------------------------------------------------------------------
    // Private Constants
    // ------------------------------------------------------------------------

    public static final Parcelable.Creator<Feature> CREATOR = new Creator<Feature>() {
        @Override
        public Feature createFromParcel(Parcel in) {
            return (Feature) readParcel(in);
        }

        @Override
        public Feature[] newArray(int size) {
            return new Feature[size];
        }
    };
    private static final String JSON_ID = "_id";
    private static final String JSON_GEOMETRY = "geometry";

    // ------------------------------------------------------------------------
    // Instance Variables
    // ------------------------------------------------------------------------
    private static final String JSON_PROPERTIES = "properties";
    private String mIdentifier;
    private Geometry mGeometry;

    // ------------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------------
    private JSONObject mProperties;

    public Feature() {
        // Default Constructor
    }

    public Feature(JSONObject json) {
        super(json);

        this.mIdentifier = JSONUtils.optString(json, JSON_ID);

        JSONObject geometry = json.optJSONObject(JSON_GEOMETRY);
        if (geometry != null) {
            this.mGeometry = (Geometry) GeoJSON.parse(geometry);
        }

        this.mProperties = json.optJSONObject(JSON_PROPERTIES);
    }

    // ------------------------------------------------------------------------
    // Parcelable Interface
    // ------------------------------------------------------------------------

    public Feature(Geometry geometry) {
        this.mGeometry = geometry;
    }

    // ------------------------------------------------------------------------
    // Public Methods
    // ------------------------------------------------------------------------

    public String getIdentifier() {
        return this.mIdentifier;
    }

    public void setIdentifier(String identifier) {
        this.mIdentifier = identifier;
    }

    public Geometry getGeometry() {
        return this.mGeometry;
    }

    public void setGeometry(Geometry geometry) {
        this.mGeometry = geometry;
    }

    public JSONObject getProperties() {
        return this.mProperties;
    }

    public void setProperties(JSONObject properties) {
        this.mProperties = properties;
    }

    @Override
    public String getType() {
        return GeoJSON.TYPE_FEATURE;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject json = super.toJSON();

        json.put(JSON_ID, this.mIdentifier);

        if (this.mGeometry != null) {
            json.put(JSON_GEOMETRY, this.mGeometry.toJSON());
        } else {
            json.put(JSON_GEOMETRY, JSONObject.NULL);
        }

        if (this.mProperties != null) {
            json.put(JSON_PROPERTIES, this.mProperties);
        } else {
            json.put(JSON_PROPERTIES, JSONObject.NULL);
        }

        return json;
    }

}
