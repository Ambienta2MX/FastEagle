package mx.ipn.ambienta2mx.fastEagle.web.RouteClosures

import groovy.json.JsonOutput

/**
 * Created by alberto on 11/10/15.
 */
class RoutesDefinition {
    def definedConfiguration
    def container
    def eventBus

    def findPlacesByLatLon = { request ->
        def coordinates = [Double.parseDouble(request.params.longitude ?: 0), Double.parseDouble(request.params.latitude ?: 0)]
        def maxDistance = Double.parseDouble(request.params.distance ?: 100)
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
                limit: maxItems
        ]
        eventBus.send("$definedConfiguration.mongo.address", query) { mongoResponse ->
            request.response.putHeader("Content-Type", "application/json")
            if (mongoResponse.body.results) {
                request.response.end "${JsonOutput.toJson(mongoResponse.body.results)}"
            } else { // No results, trying to resolve the information via google maps
                eventBus.send("$definedConfiguration.location.address", [method: 'latlng', coordinates: coordinates]) { message ->
                    def documents = [message.body]
                    request.response.end "${JsonOutput.toJson(documents)}"
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
                limit: maxItems
        ]
        eventBus.send("$definedConfiguration.mongo.address", query) { mongoResponse ->
            request.response.putHeader("Content-Type", "application/json")
            if (mongoResponse.body.results) {
                request.response.end "${JsonOutput.toJson(mongoResponse.body.results)}"
            } else { // No results, trying to resolve the information via google maps
                eventBus.send("$definedConfiguration.location.address", [method: 'name', name: name]) { message ->
                    def documents = [message.body]
                    request.response.end "${JsonOutput.toJson(documents)}"
                }
            }
        }
    } as groovy.lang.Closure

    def findPlacesBy = { request ->
        if(request.params.name) {
            return this.findPlacesByName(request)
        } else {
            return this.findPlacesByLatLon(request)
        }
    } as groovy.lang.Closure
}