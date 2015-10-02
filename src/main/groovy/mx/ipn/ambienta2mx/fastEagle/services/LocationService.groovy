package mx.ipn.ambienta2mx.fastEagle.services

import groovyx.net.http.HTTPBuilder
import mx.ipn.ambienta2mx.fastEagle.model.Place

/**
 * Created by alberto on 4/16/15.
 */
class LocationService {
    def http
    def currentKey

    LocationService(String googleApiKey) {
        this.currentKey = googleApiKey
    }

    def solvePlaceByName(def address) {
        http = new HTTPBuilder('https://maps.googleapis.com/maps/api/geocode/');
        Place place = new Place()
        http.get(path: "json", query: [address: "address", key: "$currentKey"]) { resp, json ->
            println resp.status
            println "results: "
            println json.results[-2].formatted_address
        }
    }

    def solvePlaceByLatLon(def latitude, def longitude) {
        http = new HTTPBuilder('https://maps.googleapis.com/maps/api/geocode/');
        Place place = new Place()
        http.get(path: "json", query: [latlng: "$latitude,$longitude", key: "$currentKey"]) { resp, json ->
            println resp.status
            println "results: "
            println json.results[-2].formatted_address
        }
    }
}
