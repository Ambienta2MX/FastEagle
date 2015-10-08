package mx.ipn.ambienta2mx.fastEagle.web

import mx.ipn.ambienta2mx.fastEagle.model.Place
import mx.ipn.ambienta2mx.fastEagle.services.LocationService
import org.vertx.groovy.platform.Verticle

/**
 * Created by alberto on 28/09/15.
 */
class LocationVerticle extends Verticle {
    Map definedConfiguration
    def eventBus

    LocationService locationService = new LocationService()

    def start() {
        definedConfiguration = container.getConfig()
        locationService.currentKey = definedConfiguration.GoogleMapsConfiguration.key
        container.logger.print("GoogleMapsVerticle has started")
        eventBus = vertx.eventBus
        eventBus.registerHandler("$definedConfiguration.locationVerticle.address") { message ->
            container.logger.debug(message.body);
            def mongoOperation = [action: 'save', collection: 'Places']
            Place place
            if (message.body.method == 'latlng') { // solve location by lat/lng
                place = locationService.solvePlaceByLatLon(message.body[0], message.body[1])
            } else { // solve location by place
                place = locationService.solvePlaceByName(message.body)
            }
            mongoOperation.document = place.properties
            eventBus.send("$definedConfiguration.mongo.address", mongoOperation) { result ->
                if (result.status) {
                    message.reply(place.properties)
                } else {
                    message.reply([])
                }
            };
        };
    }

    def stop() {
        container.logger.error(this.class.name + "has been stopped");
    }
}
