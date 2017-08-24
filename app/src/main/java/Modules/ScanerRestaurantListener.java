package Modules;

import java.util.List;

import Models.MarkerObject;

/**
 * Created by Ngoc Khanh on 8/18/2017.
 */

public interface ScanerRestaurantListener {
    void onScanerStart();
    void onScanerSuccess(List<MarkerObject> points);
}
