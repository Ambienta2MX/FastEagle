import com.ambienta2mx.fasteagle.services.PersistenceService

/**
 * Created by alberto on 6/9/15.
 */
class PersistenceServiceSpec extends spock.lang.Specification {
    PersistenceService persistence;

    def setup(){
        persistence = new PersistenceService("Ambienta2MX-places-test")
    }

    def cleanup(){
        persistence.ambienta2MXDB.places.drop()
    }

    def "Should insert a place into a mongo db database"() {
        expect:
        persistence.savePlace(place) == result
        where:
        place                                                                                                                                                                                                                                                                                                                                                                                || result
        [location: [type: 'Point', coordinates: [-99.168889, 19.482778]], sexagesimal_coordinates: [192858.0, 991060.0], itrf_coordinates: [-99.168889, 19.482778], nad27_coordinates: [-99.168614, 19.482116], height: 2249.0, town: 'Azcapotzalco', state: 'Distrito Federal', city: 'Azcapotzalco', fullName: 'Distrito Federal, Azcapotzalco, Azcapotzalco', zipCode: '', extraInfo: []] || true
    }

    def "Should find a place in mongo db database"() {
        expect:
        persistence.findPlace(query).fullName == result.fullName
        where:
        query                                                    || result
        [coordinates: [-99.168889, 19.482778], maxDistance: 100] || [_id: '5545972a44ae7acd64d16376', location: [type: 'Point', coordinates: [-99.168889, 19.482778]], sexagesimal_coordinates: [192858.0, 991060.0], itrf_coordinates: [-99.168889, 19.482778], nad27_coordinates: [-99.168614, 19.482116], height: 2249.0, town: 'Azcapotzalco', state: 'Distrito Federal', city: 'Azcapotzalco', fullName: 'Distrito Federal, Azcapotzalco, Azcapotzalco', zipCode: '', extraInfo: []]
    }
}
