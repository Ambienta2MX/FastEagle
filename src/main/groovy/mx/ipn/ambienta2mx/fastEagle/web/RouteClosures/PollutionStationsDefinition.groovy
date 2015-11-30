package mx.ipn.ambienta2mx.fastEagle.web.RouteClosures

import groovy.json.JsonOutput

/**
 * Created by alberto on 29/11/15.
 */
class PollutionStationsDefinition {
    def definedConfiguration
    def container
    def eventBus

    def findPollutionStations = { request ->
        /*Enabling CORS*/
        request.response.putHeader("Access-Control-Allow-Origin", "${request.headers.origin}")
        request.response.putHeader("Access-Control-Allow-Methods", "GET, OPTIONS, POST");
        request.response.putHeader("Access-Control-Allow-Headers", "Content-Type, X-Requested-With, Accept");
        request.response.putHeader("Content-Type", "application/json")
        def response
        try {
            response = this.findPollutionStationsByLatLon(request)
            return response
        } catch (Exception e) {
            e.printStackTrace()
            println(e.getMessage())
            println(e.getLocalizedMessage());
            return request.response.end("{'error' : ${e.getLocalizedMessage()}")
        }
    } as groovy.lang.Closure


    def findPollutionStationsByLatLon = { request ->
        def coordinates = [Double.parseDouble(request.params.longitude ?: "0"), Double.parseDouble(request.params.latitude ?: "0")]
        def query = [
                action : 'find', collection: 'PollutionStations',
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
                println "Response from findPollutionStationsByLatLng"
                request.response.end "${JsonOutput.toJson(mongoResponse.body.results)}"
            } else { // No results, trying to resolve the information via google maps
                request.response.end('{"status": "Pollution Station not found"}')
            }

        }
    }
}
