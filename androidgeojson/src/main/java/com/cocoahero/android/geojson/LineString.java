package com.cocoahero.android.geojson;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LineString extends Geometry {

    // ------------------------------------------------------------------------
    // Instance Variables
    // ------------------------------------------------------------------------

    public static final Parcelable.Creator<LineString> CREATOR = new Creator<LineString>() {
        @Override
        public LineString createFromParcel(Parcel in) {
            return (LineString) readParcel(in);
        }

        @Override
        public LineString[] newArray(int size) {
            return new LineString[size];
        }
    };

    // ------------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------------
    private final PositionList mPositionList = new PositionList();

    public LineString() {
        // Default Constructor
    }

    public LineString(JSONObject json) {
        super(json);

        this.setPositions(json.optJSONArray(JSON_COORDINATES));
    }

    // ------------------------------------------------------------------------
    // Parcelable Interface
    // ------------------------------------------------------------------------

    public LineString(JSONArray positions) {
        this.setPositions(positions);
    }

    // ------------------------------------------------------------------------
    // Public Methods
    // ------------------------------------------------------------------------

    public void addPosition(Position position) {
        this.mPositionList.addPosition(position);
    }

    public void removePosition(Position position) {
        this.mPositionList.removePosition(position);
    }

    public List<Position> getPositions() {
        return this.mPositionList.getPositions();
    }

    public void setPositions(JSONArray positions) {
        this.mPositionList.setPositions(positions);
    }

    public void setPositions(List<Position> positions) {
        this.mPositionList.setPositions(positions);
    }

    public boolean isLinearRing() {
        return this.mPositionList.isLinearRing();
    }

    @Override
    public String getType() {
        return GeoJSON.TYPE_LINE_STRING;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject json = super.toJSON();

        json.put(JSON_COORDINATES, this.mPositionList.toJSON());

        return json;
    }

}
