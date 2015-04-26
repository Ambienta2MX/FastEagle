package com.ambienta2mx.fasteagle

import static com.xlson.groovycsv.CsvParser.parseCsv

/**
 * Created by alberto on 4/14/15.
 */
class Validator {
    // Most left point, near Tijuana
    def final double maxLongitude = -117.083333
    def final double maxAltitude = 32.533333
    // Most right point, near Cancun
    def final double minLongitude = 21.133333
    def final double minAltitude = -86.733333

    def validateRow(def content) {
        content.LATITUD = (Integer.parseInt("${content.LATITUD ?: 0}"))
        content.LONGITUD = (-Integer.parseInt("${content.LONGITUD ?: 0}"))
        //
        if (content.LATITUD && content.LONGITUD) {
            content.valid = true
            return content
        }
        if (content?.NOMBRE_EDO && content?.NOMBRE_MUN) {
            content.valid = true
            content.solve = true
            return content
        } else {
            null
        }
    }

    def validateFile(def path) {
        def csv = new File(path).text
        def data = parseCsv(csv, autoDetect: true)
        def validated = []
        for (line in data) {
            def csvMap = [:]
            line.columns.each { key, index ->
                csvMap["$key"] = line.values[index]
            }
            def row = validateRow(csvMap)
            if (row) {
                validated.add(row)
            }
        }
        return validated
    }
}