package com.ambienta2mx.fasteagle

/**
 * Created by alberto on 4/14/15.
 */
class Validator {
    // Most left point, near Tijuana
    def final Double maxLongitude = -117.083333
    def final Double maxAltitude = 32.533333
    // Most right point, near Cancun
    def final Double minLongitude = 21.133333
    def final Double minAltitude = -86.733333

    def validate(def content) {

        if (content?.LATITUD && content?.LONGITUD) {
            content.LATITUD = Double.parseDouble("$content.LATITUD")
            content.LONGITUD = -Double.parseDouble("$content.LONGITUD")
            content.valid = true
            return content
        }
        if (content?.NOMBRE_EDO && content?.NOMBRE_MUN) {
            content.valid = true
            content.solve = true
            return content
        } else {
            content = content ?: [:]
            content?.valid = false
            return content
        }
    }
}
/*
* if (content.NOMBRE_EDO && content.NOMBRE_MUN) {
                content.valid = true
                content.solve = true
                return content
            } else {
                content.valid = false
                return content
            }*/
