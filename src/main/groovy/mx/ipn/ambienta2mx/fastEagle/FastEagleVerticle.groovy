package mx.ipn.ambienta2mx.fastEagle

import mx.ipn.ambienta2mx.fastEagle.web.LocationVerticle
import mx.ipn.ambienta2mx.fastEagle.web.QueryVerticle
import org.vertx.groovy.platform.Verticle

class FastEagleVerticle extends Verticle {
    Map definedConfiguration

    def start() {
        definedConfiguration = container.config
        container.deployModule('io.vertx~mod-mongo-persistor~2.1.0', definedConfiguration.mongo)
        // Mongo Query Verticle
        container.deployVerticle("groovy:" + QueryVerticle.class.getCanonicalName(), definedConfiguration)
        // Google Maps Solver verticle
        container.deployVerticle("groovy:" + LocationVerticle.class.getCanonicalName(), definedConfiguration)
    }

    def stop() {
        container.logger.info("Fast Eagle main verticle has stopped!");
    }
}
