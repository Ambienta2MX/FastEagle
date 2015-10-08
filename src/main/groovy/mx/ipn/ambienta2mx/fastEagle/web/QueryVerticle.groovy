package mx.ipn.ambienta2mx.fastEagle.web

import groovy.json.JsonOutput
import org.vertx.groovy.core.http.RouteMatcher
import org.vertx.groovy.platform.Verticle

/**
 * Created by alberto on 28/09/15.
 */
class QueryVerticle extends Verticle {
    Map definedConfiguration
    def eventBus
    def server
    def routeMatcher

    def start() {
        definedConfiguration = container.getConfig()
        eventBus = vertx.eventBus
        server = vertx.createHttpServer()
        routeMatcher = new RouteMatcher()
        //
        routeMatcher.get("/places/:latitude/:longitude/:maxDistance") { request ->
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
                    container.logger.info("Not found!")
                    container.logger.info("Sending information to location verticle!")
                    eventBus.send("$definedConfiguration.location.address", [method: 'latlng', coordinates: coordinates]) { message ->
                        request.response.end "${JsonOutput.toJson(message.body)}"
                    }
                }

            }
        }
        server.requestHandler(routeMatcher.asClosure()).listen(definedConfiguration.queryVerticle.http.port, definedConfiguration.queryVerticle.http.host);
    }

    def stop() {
        container.logger.error(this.class.name + "has been stopped");
    }
}
