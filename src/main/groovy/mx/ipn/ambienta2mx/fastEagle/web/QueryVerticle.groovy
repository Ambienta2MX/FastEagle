package mx.ipn.ambienta2mx.fastEagle.web

import mx.ipn.ambienta2mx.fastEagle.web.RouteClosures.PlacesDefinition
import mx.ipn.ambienta2mx.fastEagle.web.RouteClosures.PollutionStationsDefinition
import mx.ipn.ambienta2mx.fastEagle.web.RouteClosures.WeatherStationsDefinition
import mx.ipn.ambienta2mx.fastEagle.web.RouteClosures.WeatherUndergroundDefinition
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
    PlacesDefinition places = new PlacesDefinition()
    WeatherStationsDefinition weather = new WeatherStationsDefinition()
    PollutionStationsDefinition pollution = new PollutionStationsDefinition()
    WeatherUndergroundDefinition weatherUnderground = new WeatherUndergroundDefinition()
    def start() {
        definedConfiguration = container.getConfig()
        eventBus = vertx.eventBus
        server = vertx.createHttpServer()
        routeMatcher = new RouteMatcher()
        //
        places.definedConfiguration = this.definedConfiguration
        places.eventBus = vertx.eventBus
        places.container = this.container
        //
        weather.definedConfiguration = this.definedConfiguration
        weather.eventBus = vertx.eventBus
        weather.container = this.container
        //
        pollution.definedConfiguration = this.definedConfiguration
        pollution.eventBus = vertx.eventBus
        pollution.container = this.container
        //
        weatherUnderground.definedConfiguration = this.definedConfiguration
        weatherUnderground.eventBus = vertx.eventBus
        weatherUnderground.container = this.container
        //
        routeMatcher.all("/places", places.findPlacesBy)
        routeMatcher.all("/conaguaStation", weather.findWeatherStations)
        routeMatcher.all("/pollutionStation", pollution.findPollutionStations)
        routeMatcher.all("/weatherUndergroundStation", weatherUnderground.findWeatherUndergroundStations)
        server.requestHandler(routeMatcher.asClosure()).listen(definedConfiguration.queryVerticle.http.port, definedConfiguration.queryVerticle.http.host);
    }

    def stop() {
        container.logger.error(this.class.name + "has been stopped");
    }
}
