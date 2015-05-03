import com.ambienta2mx.fasteagle.Solver

/**
 * Created by alberto on 4/25/15.
 */
class SolverSpec extends spock.lang.Specification {
    Solver solver = new Solver()

    def "Should solve a location"() {
        expect:
        solver.findSuitablePlace(place) == result
        where:
        place                                                                      || result
        ['LATITUD': '191658', 'LONGITUD': '0991716', 'valid': true, 'solve': true] || ['LATITUD': 0, 'LONGITUD': 0, 'NOMBRE_EDO': 'Distrito Federal', 'NOMBRE_MUN': 'Azcapotzalco', 'valid': true, 'solve': true]
        ['LATITUD': '191658', 'LONGITUD': '0991716', 'valid': true, 'solve': true] || ['LATITUD': 0, 'LONGITUD': 0, 'NOMBRE_EDO': 'Distrito Federal', 'NOMBRE_MUN': 'Azcapotzalco', 'valid': true, 'solve': true]
    }

    def "Should get itrf/nad27 coordinates"() {
        expect:
        solver.getGeographicCoordinates(place) == result
        where:
        place                                                                                                                       || result
        ['LATITUD': 0, 'LONGITUD': 0, 'NOMBRE_EDO': 'Distrito Federal', 'NOMBRE_MUN': 'Azcapotzalco', 'valid': true, 'solve': true] || ['LATITUD': 0, 'LONGITUD': 0, 'NOMBRE_EDO': 'Distrito Federal', 'NOMBRE_MUN': 'Azcapotzalco', 'valid': true, 'solve': true]
    }
}
