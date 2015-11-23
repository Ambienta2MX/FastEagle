package mx.ipn.ambienta2mx.fastEagle.services

import groovyx.net.http.HTTPBuilder
import mx.ipn.ambienta2mx.fastEagle.model.Place
import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * Created by alberto on 4/16/15.
 */
class LocationService {
    def http
    def currentKey
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
    LocationService(String googleApiKey) {
        this.currentKey = googleApiKey
    }

    Map solvePlaceByLatLon(def longitude, def latitude) {
        def place = [:]
        http = new HTTPBuilder('https://maps.googleapis.com/maps/api/geocode/');
        http.get(path: "json", query: [latlng: "$latitude,$longitude", key: "$currentKey", "language": "es"]) { resp, json ->
            // Avoiding a null response
            if (json.results) {
                // Parsing the Json information from google
                def location = json.results[0].geometry.location
                List coordinates = [location.lng, location.lat]
                def addressComponents = json.results[0].address_components
                // Retrieving just information from Mexico
                if (json.results[-1].address_components[0].short_name == "MX") {
                    place.itrf_coordinates = coordinates
                    place.nad27_coordinates = coordinates
                    place.location = [type: "Point", coordinates: coordinates]
                    place.sexagesimal_coordinates = [] // Just INEGI Sources contains this information

                    place.zipCode = addressComponents.find { element -> "postal_code" in element.types }?.long_name ?: ""
                    place.state = addressComponents.find { element -> "administrative_area_level_1" in element.types }?.long_name ?: ""
                    place.city = addressComponents.find { element -> "administrative_area_level_3" in element.types }?.long_name ?: ""
                    place.town = addressComponents.find { element -> "sublocality" in element.types }?.long_name ?: ""
                    place.fullName = "$place.state, $place.city, $place.town"
                }
            }
        }
        place.dateCreated = format.format(new Date())
        place.lastUpdated = format.format(new Date())
        place.extraInfo = ["Solved using Google Maps geocode service."]
        // TODO Solve place height
        place.provider = ["Google Maps"]
        return place
    }

    Map solvePlaceByName(def address) {
        http = new HTTPBuilder('https://maps.googleapis.com/maps/api/geocode/');
        def place = [:]
        http.get(path: "json", query: [address: "$address", key: "$currentKey"]) { resp, json ->
            // Avoiding a null response
            if (json.results) {
                // Parsing the Json information from google
                def location = json.results[0].geometry.location
                List coordinates = [location.lng, location.lat]
                def addressComponents = json.results[0].address_components
                // Retrieving just information from Mexico
                if (json.results[-1].address_components[-1].short_name == "MX") {
                    place.itrf_coordinates = coordinates
                    place.nad27_coordinates = coordinates
                    place.location = [type: "Point", coordinates: coordinates]
                    place.sexagesimal_coordinates = [] // Just INEGI Sources contains this information

                    place.zipCode = addressComponents.find { element -> "postal_code" in element.types }?.long_name ?: ""
                    place.state = addressComponents.find { element -> "administrative_area_level_1" in element.types }?.long_name ?: ""
                    place.city = addressComponents.find { element -> "administrative_area_level_3" in element.types }?.long_name ?: ""
                    place.town = addressComponents.find { element -> "sublocality" in element.types }.long_name ?: ""
                    place.fullName = "$place.state, $place.city, $place.town"
                }
            }
        }
        place.dateCreated = new Date().toString()
        place.lastUpdated = new Date().toString()
        place.extraInfo = ["Solved using Google Maps geocode service."]
        // TODO Solve place height
        place.provider = ["Google Maps"]

        return place
    }
}
