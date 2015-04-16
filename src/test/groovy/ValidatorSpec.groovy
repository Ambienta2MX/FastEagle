import com.ambienta2mx.fasteagle.Validator
import spock.lang.Shared

/**
 * Created by alberto on 4/14/15.
 */
class ValidatorSpec extends spock.lang.Specification {
    @Shared
    Validator validator = new Validator()

    def "should validate the content of a row inside a csv file"() {
        expect:
        validator.validate(rowContent) == result
        where:
        /*
        * Let's consider the next headers
        * "CLAVE","CVE_GEOEST","CVE_AGEB","LATITUD","LONGITUD","ALTITUD","CARTA_TOPO","TIPO","NOMBRE_EDO","NOMBRE_MUN"
        * Needed ones: "LATITUD","LONGITUD"
        * Desired: "LATITUD","LONGITUD","ALTITUD","NOMBRE_EDO","NOMBRE_MUN"
        * */
        header                       | rowContent || result
        ['']                         | ['']       || false
        ['LATITUD', 'LONGITUD']      | ['0', '0'] || false
        ['LATITUD', 'LONGITUD']      | ['0', '0'] || false
        ['NOMBRE_EDO', 'NOMBRE_MUN'] | ['0', '0'] || false

    }
}
