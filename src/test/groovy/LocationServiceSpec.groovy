import mx.ipn.ambienta2mx.fastEagle.services.LocationService

/**
 * Created by alberto on 4/25/15.
 */
class LocationServiceSpec extends spock.lang.Specification {

    def "Should solve a location via Lat/Lon"() {
        given:
        LocationService solver = new LocationService("AIzaSyCo72iVxPewCLtXKmoeiWSyNAAVTqIiVvs")
        expect:
        Map solverResponse = solver.solvePlaceByLatLon(latitude, longitude)
        solverResponse.fullName == result.fullName
        where:
        longitude  | latitude   || result
        -99.186510 | 19.504434  || [fullName: 'Distrito Federal, Azcapotzalco, San Pablo Xalpa']
        -99.019232 | 19.3740416 || [fullName: 'Distrito Federal, Iztapalapa, Zona Urbana Ejidal Santa Martha Acatitla Sur']
        -25.019232 | 88.3740416 || [fullName: null]
    }

    def "Should solve a location via Place"() {
        given:
        LocationService solver = new LocationService("AIzaSyCo72iVxPewCLtXKmoeiWSyNAAVTqIiVvs")
        expect:
        solver.solvePlaceByName(place).itrf_coordinates == result.itrf_coordinates
        where:
        place                                                           || result
        ['fullName': 'Distrito Federal, Azcapotzalco, San Pablo Xalpa'] || ['itrf_coordinates': [-99.186510, 19.504434]]
        ['fullName': 'Distrito Federal, Gustavo A. Madero, Lindavista'] || ['itrf_coordinates': [-99.146569, 19.504664]]
        ['fullName': 'Ahuehuetes La perla Nezahualcóyotl']              || ['itrf_coordinates': [-98.9926226, 19.3866537]]
        ['fullName': 'lugar que no existe']                             || ['itrf_coordinates': null]
    }
}
