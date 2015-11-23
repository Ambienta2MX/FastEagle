package mx.ipn.ambienta2mx.fastEagle

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
        latitude   | longitude  || result
        -99.186510 | 19.504434  || [fullName: 'Distrito Federal, Azcapotzalco, San Pablo Xalpa']
        -99.019232 | 19.3740416 || [fullName: 'Distrito Federal, Iztapalapa, Zona Urbana Ejidal Santa Martha Acatitla Sur']
        -25.019232 | 88.3740416 || [fullName: null]
    }

    def "Should solve a location via Place"() {
        given:
        LocationService solver = new LocationService("AIzaSyCo72iVxPewCLtXKmoeiWSyNAAVTqIiVvs")
        expect:
        def placeFromMaps = solver.solvePlaceByName(place)
        println placeFromMaps

        def differenceLng = Math.abs(placeFromMaps.itrf_coordinates[0] - result.itrf_coordinates[0])
        def differenceLat = Math.abs(placeFromMaps.itrf_coordinates[1] - result.itrf_coordinates[1])
        assert differenceLng < 0.01 && differenceLat < 0.01

        where:
        place                                                           || result
        ['fullName': 'Distrito Federal, Azcapotzalco, San Pablo Xalpa'] || ['itrf_coordinates': [-99.1867903, 19.5054771]]
        ['fullName': 'Ahuehuetes La perla Nezahualcóyotl']              || ['itrf_coordinates': [-98.9926226, 19.3866537]]
        //['fullName': 'lugar que no existe']                             || ['itrf_coordinates': null]
    }
}
