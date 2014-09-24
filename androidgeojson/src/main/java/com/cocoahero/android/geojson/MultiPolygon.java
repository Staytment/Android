package com.cocoahero.android.geojson;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MultiPolygon extends Geometry {

    // ------------------------------------------------------------------------
    // Instance Variables
    // ------------------------------------------------------------------------

    public static final Parcelable.Creator<MultiPolygon> CREATOR = new Creator<MultiPolygon>() {
        @Override
        public MultiPolygon createFromParcel(Parcel in) {
            return (MultiPolygon) readParcel(in);
        }

        @Override
        public MultiPolygon[] newArray(int size) {
            return new MultiPolygon[size];
        }
    };

    // ------------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------------
    private final List<Polygon> mPolygons = new ArrayList<Polygon>();

    public MultiPolygon() {
        // Default Constructor
    }

    // ------------------------------------------------------------------------
    // Parcelable Interface
    // ------------------------------------------------------------------------

    public MultiPolygon(JSONObject json) {
        super(json);

        this.setPolygons(json.optJSONArray(JSON_COORDINATES));
    }

    // ------------------------------------------------------------------------
    // Public Methods
    // ------------------------------------------------------------------------

    public void addPolygon(Polygon polygon) {
        this.mPolygons.add(polygon);
    }

    public void removePolygon(Polygon polygon) {
        this.mPolygons.remove(polygon);
    }

    public List<Polygon> getPolygons() {
        return this.mPolygons;
    }

    public void setPolygons(List<Polygon> polygons) {
        this.mPolygons.clear();
        if (polygons != null) {
            this.mPolygons.addAll(polygons);
        }
    }

    public void setPolygons(JSONArray polygons) {
        this.mPolygons.clear();
        if (polygons != null) {
            for (int i = 0; i < polygons.length(); i++) {
                JSONArray polyJSON = polygons.optJSONArray(i);
                if (polyJSON != null) {
                    this.mPolygons.add(new Polygon(polyJSON));
                }
            }
        }
    }

    @Override
    public String getType() {
        return GeoJSON.TYPE_MULTI_POLYGON;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject json = super.toJSON();

        JSONArray polygons = new JSONArray();
        for (Polygon polygon : this.mPolygons) {
            JSONArray ringJSON = new JSONArray();
            for (Ring ring : polygon.getRings()) {
                ringJSON.put(ring.toJSON());
            }
            polygons.put(ringJSON);
        }
        json.put(JSON_COORDINATES, polygons);

        return json;
    }

}
