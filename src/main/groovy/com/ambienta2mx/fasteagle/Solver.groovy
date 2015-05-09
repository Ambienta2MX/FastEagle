package com.ambienta2mx.fasteagle

import groovyx.net.http.HTTPBuilder

/**
 * Created by alberto on 4/16/15.
 */
class Solver {
    def http
    def solvePlaceByName(def place) {
        http = new HTTPBuilder('https://maps.googleapis.com/maps/api/geocode/');

    }

    def solvePlaceByLatLon(def latitude, def longitude) {
        http = new HTTPBuilder('https://maps.googleapis.com/maps/api/geocode/');
    }
}
