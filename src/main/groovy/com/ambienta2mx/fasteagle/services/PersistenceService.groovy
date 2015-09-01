package com.ambienta2mx.fasteagle.services

import com.gmongo.GMongo

/**
 * Created by alberto on 6/9/15.
 */
class PersistenceService {
    def ambienta2MXDB
    def PersistenceService(def database){
        ambienta2MXDB = new GMongo().getDB(database)
    }

    def PersistenceService(def host,def database){
        ambienta2MXDB = new GMongo(host).getDB(database)
    }

    def findPlace(def query) {
        return ambienta2MXDB.places.findOne(
            'location': [
                '$near': [
                    '$geometry'   : [
                        type: "Point", coordinates: [query.coordinates]
                    ],
                    '$maxDistance': query.maxDistance ?: Double.parseDouble("100")
                ]
            ]
        )
    }

    def savePlace(def place) {
        return (ambienta2MXDB.places << place) =~ "N/A"
    }
}
