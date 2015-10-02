import mx.ipn.ambienta2mx.fastEagle.services.LocationService

/**
 * Created by alberto on 4/25/15.
 */
class LocationServiceSpec extends spock.lang.Specification {
    LocationService solver = new LocationService()

    def "Should solve a location via Lat/Lon"() {
        expect:
        solver.solvePlaceByLatLon(latitude, longitude) == result
        where:
        latitude   | longitude || result
        -99.186510 | 19.504434 || [fullName: 'Distrito Federal, Azcapotzalco, San Pablo Xalpa']
    }

    def "Should solve a location via Place"() {
        expect:
        solver.solvePlaceByName(place) == result
        where:
        place                                                           || result
        ['fullName': 'Distrito Federal, Azcapotzalco, San Pablo Xalpa'] || ['coordinates': [-99.186510, 19.504434]]
        ['fullName': 'Distrito Federal, Gustavo A. Madero, Lindavista'] || ['coordinates': [-99.146569, 19.504664]]
    }
}
