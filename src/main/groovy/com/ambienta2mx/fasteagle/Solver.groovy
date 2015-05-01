package com.ambienta2mx.fasteagle

import groovyx.net.http.HTTPBuilder

/**
 * Created by alberto on 4/16/15.
 */
class Solver {
    def http
    def findSuitablePlace(def place) {
        http = new HTTPBuilder('https://maps.googleapis.com/maps/api/geocode/');
        def latitude = place.latitud/10000 +

    }

    def getGeographicCoordinates(def place) {
        http  = new HTTPBuilder('https://maps.googleapis.com/maps/api/geocode/')
    }
}
