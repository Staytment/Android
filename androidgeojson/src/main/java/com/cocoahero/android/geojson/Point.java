package com.cocoahero.android.geojson;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Point extends Geometry {

    // ------------------------------------------------------------------------
    // Instance Variables
    // ------------------------------------------------------------------------

    public static final Parcelable.Creator<Point> CREATOR = new Creator<Point>() {
        @Override
        public Point createFromParcel(Parcel in) {
            return (Point) readParcel(in);
        }

        @Override
        public Point[] newArray(int size) {
            return new Point[size];
        }
    };

    // ------------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------------
    private Position mPosition;

    public Point() {
        // Default Constructor
    }

    public Point(JSONObject json) {
        super(json);

        this.setPosition(json.optJSONArray(JSON_COORDINATES));
    }

    public Point(JSONArray position) {
        this.setPosition(position);
    }

    public Point(Position position) {
        this.setPosition(position);
    }

    public Point(double latitude, double longitude) {
        this.mPosition = new Position(latitude, longitude);
    }

    // ------------------------------------------------------------------------
    // Parcelable Interface
    // ------------------------------------------------------------------------

    public Point(double latitude, double longitude, double altitude) {
        this.mPosition = new Position(latitude, longitude, altitude);
    }

    // ------------------------------------------------------------------------
    // Public Methods
    // ------------------------------------------------------------------------

    public Position getPosition() {
        return this.mPosition;
    }

    public void setPosition(Position position) {
        this.mPosition = position;
    }

    public void setPosition(JSONArray position) {
        if (position != null) {
            this.mPosition = new Position(position);
        } else {
            this.mPosition = null;
        }
    }

    @Override
    public String getType() {
        return GeoJSON.TYPE_POINT;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject json = super.toJSON();

        if (this.mPosition != null) {
            json.put(JSON_COORDINATES, this.mPosition.toJSON());
        }

        return json;
    }

}
