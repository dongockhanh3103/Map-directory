package Models;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Ngoc Khanh on 8/18/2017.
 */

public class MarkerObject {
    LatLng latLng;
    String titles;
    public MarkerObject(){}
    public MarkerObject(LatLng latLng,String titles){
        this.latLng=latLng;
        this.titles=titles;
    }


    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getTitles() {
        return titles;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }


}
