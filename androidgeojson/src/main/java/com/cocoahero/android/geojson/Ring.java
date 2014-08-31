package com.cocoahero.android.geojson;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;

public class Ring extends PositionList {

    // ------------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------------

    public static final Parcelable.Creator<Ring> CREATOR = new Parcelable.Creator<Ring>() {
        @Override
        public Ring createFromParcel(Parcel in) {
            return new Ring(in);
        }

        @Override
        public Ring[] newArray(int size) {
            return new Ring[size];
        }
    };

    public Ring() {
        // Default Constructor
    }

    public Ring(JSONArray positions) {
        super(positions);
    }

    public Ring(double[][] positions) {
        super(positions);
    }

    // ------------------------------------------------------------------------
    // Parcelable Interface
    // ------------------------------------------------------------------------

    protected Ring(Parcel parcel) {
        super(parcel);
    }

    // ------------------------------------------------------------------------
    // Public Methods
    // ------------------------------------------------------------------------

    public void close() {
        if (!this.isLinearRing()) {
            this.addPosition(this.getHead());
        }
    }

}
