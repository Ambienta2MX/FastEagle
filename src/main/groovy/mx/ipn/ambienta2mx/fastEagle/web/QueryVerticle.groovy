package mx.ipn.ambienta2mx.fastEagle.web

import groovy.json.JsonOutput
import mx.ipn.ambienta2mx.fastEagle.web.RouteClosures.RoutesDefinition
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
    RoutesDefinition routes = new RoutesDefinition()

    def start() {
        definedConfiguration = container.getConfig()
        eventBus = vertx.eventBus
        server = vertx.createHttpServer()
        routeMatcher = new RouteMatcher()

        routes.definedConfiguration = this.definedConfiguration
        routes.eventBus = vertx.eventBus
        routes.container = this.container

        //
        routeMatcher.get("/places/:latitude/:longitude/:maxDistance", routes.byLatLon)
        routeMatcher.get("/places/", routes.byName)
        server.requestHandler(routeMatcher.asClosure()).listen(definedConfiguration.queryVerticle.http.port, definedConfiguration.queryVerticle.http.host);
    }

    def stop() {
        container.logger.error(this.class.name + "has been stopped");
    }
}
