import com.ambienta2mx.fasteagle.Validator

/**
 * Created by alberto on 4/14/15.
 */
class ValidatorSpec extends spock.lang.Specification {

    Validator validator = new Validator()

    def "should validate the content of a row inside a csv file"() {
        expect:
        validator.validateRow(rowContent) == result
        where:
        /*
        * Let's consider the next headers
        * "CLAVE","CVE_GEOEST","CVE_AGEB","LATITUD","LONGITUD","ALTITUD","CARTA_TOPO","TIPO","NOMBRE_EDO","NOMBRE_MUN"
        * Needed ones: "LATITUD","LONGITUD"
        * Desired: "LATITUD","LONGITUD","ALTITUD","NOMBRE_EDO","NOMBRE_MUN"
        * */
        rowContent                                                                                      || result
        null                                                                                            || null
        ['LATITUD': 0, 'LONGITUD': 10]                                                                  || null
        ['LATITUD': "", 'LONGITUD': ""]                                                                 || null
        ['LATITUD': 10, 'LONGITUD': 10, 'NOMBRE_EDO': 'Distrito Federal', 'NOMBRE_MUN': 'Azcapotzalco'] || ['LATITUD': 10, 'LONGITUD': -10, 'NOMBRE_EDO': 'Distrito Federal', 'NOMBRE_MUN': 'Azcapotzalco', 'valid': true]
        ['NOMBRE_EDO': 'Distrito Federal', 'NOMBRE_MUN': 'Azcapotzalco']                                || ['NOMBRE_EDO': 'Distrito Federal', 'NOMBRE_MUN': 'Azcapotzalco', 'valid': true, 'solve': true]

    }

    def "should read a csv file and return just the valid rows"() {
        expect:
        validator.validateFile(file) == result
        where:
        file                       || result
        '/home/alberto/sample.csv' || [
                ["CLAVE"     : "1",
                 "CVE_GEOEST": "010010001",
                 "CVE_AGEB"  : "022-9",
                 "LATITUD"   : 215251,
                 "LONGITUD"  : -1021746,
                 "ALTITUD"   : "1870",
                 "CARTA_TOPO": "F13D19",
                 "TIPO"      : "U",
                 "NOMBRE_EDO": "Aguascalientes",
                 "NOMBRE_MUN": "Aguascalientes",
                 "valid"     : true],
                ["CLAVE"     : "2",
                 "CVE_GEOEST": "010010063",
                 "CVE_AGEB"  : "004-0",
                 "LATITUD"   : 0,
                 "LONGITUD"  : 0,
                 "ALTITUD"   : "1870",
                 "CARTA_TOPO": "F13B89",
                 "TIPO"      : "R",
                 "NOMBRE_EDO": "RESOLVER",
                 "NOMBRE_MUN": "RESOLVER",
                 "valid"     : true,
                 "solve"     : true]
        ]
    }
}
