package mx.ipn.ambienta2mx.fastEagle.web.RouteClosures

import groovy.json.JsonOutput
import org.apache.commons.collections.Closure

/**
 * Created by alberto on 11/10/15.
 */
class RoutesDefinition {
    def definedConfiguration
    def container
    def eventBus

    def byLatLon = { request ->
        def coordinates = [Double.parseDouble(request.params.latitude ?: 0), Double.parseDouble(request.params.longitude ?: 0)]
        def maxDistance = Double.parseDouble(request.params.maxDistance ?: 100)
        def query = [
                action : 'find', collection: 'Places',
                matcher: [
                        location: [
                                '$near': [
                                        '$geometry'   : [type: "Point", coordinates: coordinates],
                                        '$maxDistance': maxDistance
                                ]
                        ]
                ]
        ]
        eventBus.send("$definedConfiguration.mongo.address", query) { mongoResponse ->
            request.response.putHeader("Content-Type", "application/json")
            if (mongoResponse.body.results) {
                request.response.end "${JsonOutput.toJson(mongoResponse.body.results)}"
            } else { // No results, trying to resolve the information via google maps
                eventBus.send("$definedConfiguration.location.address", [method: 'latlng', coordinates: coordinates]) { message ->
                    request.response.end "${JsonOutput.toJson(message.body)}"
                }
            }

        }
    } as groovy.lang.Closure


    def byName = { request ->
        def name = request.params.name
        def query = [
                action : 'find', collection: 'Places',
                matcher: [
                        fullName: [
                                '$regex': name
                        ]
                ]
        ]
        eventBus.send("$definedConfiguration.mongo.address", query) { mongoResponse ->
            request.response.putHeader("Content-Type", "application/json")
            if (mongoResponse.body.results) {
                request.response.end "${JsonOutput.toJson(mongoResponse.body.results)}"
            } else { // No results, trying to resolve the information via google maps
                eventBus.send("$definedConfiguration.location.address", [method: 'name', name: name]) { message ->
                    request.response.end "${JsonOutput.toJson(message.body)}"
                }
            }
        }
    } as groovy.lang.Closure
}