package mx.ipn.ambienta2mx.fastEagle.web.RouteClosures

import groovy.json.JsonOutput

/**
 * Created by alberto on 11/10/15.
 */
class PlacesDefinition {
    def definedConfiguration
    def container
    def eventBus

    def findPlacesBy = { request ->
        /*Enabling CORS*/
        request.response.putHeader("Access-Control-Allow-Origin", "${request.headers.origin}")
        request.response.putHeader("Access-Control-Allow-Methods", "GET, OPTIONS, POST");
        request.response.putHeader("Access-Control-Allow-Headers", "Content-Type, X-Requested-With, Accept");
        request.response.putHeader("Content-Type", "application/json")
        def response
        try {
            if (request.params.name) {
                response = this.findPlacesByName(request)
            } else {
                response = this.findPlacesByLatLon(request)
            }
            return response
        } catch (Exception e) {
            e.printStackTrace()
            println(e.getMessage())
            println(e.getLocalizedMessage());
            return request.response.end("{'error' : ${e.getLocalizedMessage()}")
        }
    } as groovy.lang.Closure

    def findPlacesByLatLon = { request ->
        def coordinates = [Double.parseDouble(request.params.longitude ?: "0"), Double.parseDouble(request.params.latitude ?: "0")]
        def maxDistance = Double.parseDouble(request.params.distance ?: "100")
        def maxItems = Integer.parseInt(request.params.max ?: "10")
        def query = [
                action : 'find', collection: 'Places',
                matcher: [
                        location: [
                                '$near': [
                                        '$geometry'   : [type: "Point", coordinates: coordinates],
                                        '$maxDistance': maxDistance
                                ]
                        ]
                ],
                limit  : maxItems
        ]
        eventBus.send("$definedConfiguration.mongo.address", query) { mongoResponse ->
            if (mongoResponse.body.results) {
                println "Response from findPlacesByLatLng"
                request.response.end "${JsonOutput.toJson(mongoResponse.body.results)}"
            } else { // No results, trying to resolve the information via google maps
                eventBus.send("$definedConfiguration.location.address", [method: 'latlng', coordinates: coordinates]) { message ->
                    def id = message.body._id
                    def queryid = [
                            action : 'find', collection: 'Places',
                            matcher: [
                                    "_id": id
                            ]
                    ]
                    eventBus.send("$definedConfiguration.mongo.address", queryid) { idResponse ->
                        request.response.end "${JsonOutput.toJson(idResponse.body.results)}"
                    }
                }
            }

        }
    } as groovy.lang.Closure

    def findPlacesByName = { request ->
        def name = request.params.name
        def maxItems = Integer.parseInt(request.params.max ?: "10")
        def query = [
                action : 'find', collection: 'Places',
                matcher: [
                        fullName: [
                                '$regex': name
                        ]
                ],
                limit  : maxItems
        ]
        eventBus.send("$definedConfiguration.mongo.address", query) { mongoResponse ->
            if (mongoResponse.body.results) {
                println "Response from findPlacesByName"
                request.response.end "${JsonOutput.toJson(mongoResponse.body.results)}"
            } else { // No results, trying to resolve the information via google maps
                eventBus.send("$definedConfiguration.location.address", [method: 'name', name: name]) { message ->
                    def id = message.body._id
                    def queryid = [
                            action : 'find', collection: 'Places',
                            matcher: [
                                    "_id": id
                            ]
                    ]
                    eventBus.send("$definedConfiguration.mongo.address", queryid) { idResponse ->
                        request.response.end "${JsonOutput.toJson(idResponse.body.results)}"
                    }
                }
            }
        }
    } as groovy.lang.Closure
}