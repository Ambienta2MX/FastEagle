package mx.ipn.ambienta2mx.fastEagle.web.RouteClosures

import groovy.json.JsonOutput
import mx.ipn.ambienta2mx.fastEagle.enums.StateCode
import mx.ipn.ambienta2mx.fastEagle.services.WeatherFileService

/**
 * Created by alberto on 29/11/15.
 */
class WeatherStationsDefinition {

    def definedConfiguration
    def container
    def eventBus

    WeatherFileService weatherFileService = new WeatherFileService()

    def findWeatherStations = { request ->
        /*Enabling CORS*/
        request.response.putHeader("Access-Control-Allow-Origin", "${request.headers.origin}")
        request.response.putHeader("Access-Control-Allow-Methods", "GET, OPTIONS, POST");
        request.response.putHeader("Access-Control-Allow-Headers", "Content-Type, X-Requested-With, Accept");
        request.response.putHeader("Content-Type", "application/json")
        def response
        try {
            if (request.params.verify) {
                response = this.executeInsertionProcess(request)
            } else {
                response = this.findWeatherStationsByLatLon(request)
            }

            return response
        } catch (Exception e) {
            e.printStackTrace()
            println(e.getMessage())
            println(e.getLocalizedMessage());
            return request.response.end("{'error' : ${e.getLocalizedMessage()}")
        }
    } as groovy.lang.Closure

    def executeInsertionProcess = { request ->
        def query = [
                action: 'find', collection: 'WeatherStations',
                limit : 1
        ]
        def insertion = [action: 'save', collection: 'WeatherStations']
        eventBus.send("$definedConfiguration.mongo.address", query) { mongoResponse ->
            if (mongoResponse.body.results.size > 0) {
                request.response.end('{"status": "inserted"}')
            } else {
                StateCode.metaClass.static.values = { [StateCode.TL, StateCode.DF, StateCode.BC] } //Tlaxcala, DF, BC
                def countryUrls = weatherFileService.getFileUrlsOfCountry()
                for (item in countryUrls) {
                    insertion.document = [
                            location: [type: "Point", coordinates: [item.longitude, item.latitude]],
                            url     : item.url,
                            provider: "CONAGUA"
                    ]
                    eventBus.send("$definedConfiguration.mongo.address", insertion)
                    println ">>> " + item
                }

            }
            request.response.end('{"status":"inserted"}')
        }
    }

    def findWeatherStationsByLatLon = { request ->
        def coordinates = [Double.parseDouble(request.params.longitude ?: "0"), Double.parseDouble(request.params.latitude ?: "0")]
        def query = [
                action : 'find', collection: 'WeatherStations',
                matcher: [
                        location: [
                                '$near': [
                                        '$geometry': [type: "Point", coordinates: coordinates],
                                ]
                        ]
                ],
                limit  : 1
        ]
        eventBus.send("$definedConfiguration.mongo.address", query) { mongoResponse ->
            if (mongoResponse.body.results) {
                println "Response from findWeatherStationsByLatLng"
                request.response.end "${JsonOutput.toJson(mongoResponse.body.results)}"
            } else { // No results, trying to resolve the information via google maps
                request.response.end('{"status": "Weather Station not found"}')
            }

        }
    }
}
