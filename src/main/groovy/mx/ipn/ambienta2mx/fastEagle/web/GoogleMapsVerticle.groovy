package mx.ipn.ambienta2mx.fastEagle.web

import mx.ipn.ambienta2mx.fastEagle.services.LocationService
import org.vertx.groovy.platform.Verticle

/**
 * Created by alberto on 28/09/15.
 */
class GoogleMapsVerticle extends Verticle {
    Map definedConfiguration
    def eventBus

    LocationService locationService = new LocationService()

    def start() {
        definedConfiguration = container.getConfig()
        container.logger.print("GoogleMapsVerticle has started")
        eventBus = vertx.eventBus
        eventBus.registerHandler("Ambienta2MX.GoogleMaps")
    }

    def stop() {
        container.logger.error(this.class.name + "has been stopped");
    }
}
