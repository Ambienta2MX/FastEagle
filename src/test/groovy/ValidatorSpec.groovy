import com.ambienta2mx.fasteagle.Validator

/**
 * Created by alberto on 4/14/15.
 */
class ValidatorSpec extends spock.lang.Specification {

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
        rowContent                                                                                      || result
        null                                                                                            || [valid: false]
        ['LATITUD': 0, 'LONGITUD': 10]                                                                  || ['LATITUD': 0, 'LONGITUD': 10, 'valid': false]
        ['LATITUD': 10, 'LONGITUD': 10, 'NOMBRE_EDO': 'Distrito Federal', 'NOMBRE_MUN': 'Azcapotzalco'] || ['LATITUD': 10, 'LONGITUD': -10, 'NOMBRE_EDO': 'Distrito Federal', 'NOMBRE_MUN': 'Azcapotzalco', 'valid': true]
        ['NOMBRE_EDO': 'Distrito Federal', 'NOMBRE_MUN': 'Azcapotzalco']                                || ['NOMBRE_EDO': 'Distrito Federal', 'NOMBRE_MUN': 'Azcapotzalco', 'valid': true, 'solve': true]

    }
}
