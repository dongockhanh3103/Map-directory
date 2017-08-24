package Modules;

import java.util.List;

import Models.Route;

/**
 * Created by Ngoc Khanh on 8/4/2017.
 */
public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
