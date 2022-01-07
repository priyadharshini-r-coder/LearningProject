package com.example.learningproject.entireactivity.OtherLocation;


import com.directions.route.Route;
import com.directions.route.RouteException;

import java.util.List;

public interface RoutingListener {
    void onRoutingFailure(RouteException e);

    void onRoutingStart();

    void onRoutingSuccess(List<Route> route, int shortestRouteIndex);

    void onRoutingCancelled();
}
