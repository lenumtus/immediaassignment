package com.example.pierrot.immediaassignment.model;

/**
 * Created by Pierrot on 9/4/2015.
 */
public class Area {
    private static String city;

    public Area(String city) {
        Area.city=city;
    }

    public static String getCity() {
        return city;
    }

    public static void setCity(String city) {
        Area.city = city;
    }
}
