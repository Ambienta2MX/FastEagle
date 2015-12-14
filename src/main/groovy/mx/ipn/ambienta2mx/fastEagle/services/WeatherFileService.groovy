package mx.ipn.ambienta2mx.fastEagle.services

import groovy.json.JsonSlurper
import mx.ipn.ambienta2mx.fastEagle.enums.StateCode
import org.ccil.cowan.tagsoup.Parser

class WeatherFileService {

    final minLongitude = 85
    final maxLongitude = 120

    def getFileUrlsForStation(StateCode stateCode) {
        def stationUrls = []
        //TODO: Externalize CONAGUA hostName
        def stations = []
        def hostName = "smn.cna.gob.mx"
        def formedUrl = "emas/${stateCode.key}MP10T"
        def url = "http://${hostName}/${formedUrl}.html"

        def slurper = new XmlSlurper(new Parser())
        def htmlParse = slurper.parse(url)
        stationUrls = htmlParse.'**'.find { it.@id == 'mapa_solo' }.div.findAll {
            it.@id.toString().startsWith("estacion")
        }*.@onclick
        stationUrls.each { station ->
            stations << (station.toString() =~ /\/.*.htm/)[0]
        }
        stations = stations*.replace("estac", "txt")
        stations.collect { station ->
            "http://${hostName}${station.replace(/10.htm/, "_10M.TXT")}".toString()
        }
    }

    def convertCoordinatesToDecimal(def degressLatitude, def degressLongitude) {
      def jsonSlurper = new JsonSlurper()
      def latitudeValues = degressLatitude.collect { new Float(it) }
      def longitudeValues = degressLongitude.collect { new Float(it) }
      def decimalLatitude = new BigDecimal(latitudeValues[0] + latitudeValues[1] / 60 + latitudeValues[2] / 3600).setScale(4, BigDecimal.ROUND_HALF_UP)
      def decimalLongitude = new BigDecimal(longitudeValues[0] + longitudeValues[1] / 60 + longitudeValues[2] / 3600).setScale(4, BigDecimal.ROUND_HALF_UP)
      def jsonStructure = [:]
      if(decimalLongitude > minLongitude && decimalLongitude < maxLongitude){
        def connection = new URL("http://mapserver.inegi.org.mx/traninv/servicios/geo/itrf92/${decimalLongitude}/${decimalLatitude}")
        jsonStructure = jsonSlurper.parseText(connection.text)[0].itrf92
      }

      [latitude : jsonStructure?.y ?: 0,
       longitude: jsonStructure?.x ?: 0]
    }

    def getFileUrlsOfCountry() {
      def countryFileUrls = []

      StateCode.values().each { stateCode ->
        getFileUrlsForStation(stateCode).each { url ->
          def urlInfo = getUrlCoordinates(url)
          urlInfo.url = url
          urlInfo.source = "CONAGUA"
          countryFileUrls << urlInfo
        }
      }

        countryFileUrls
    }

    def getUrlCoordinates(String fileUrl) {
      def url = fileUrl
      try{
        def writer = url.toURL().filterLine { it ==~  /.*[0-9].*[\"|''].*/ }
        def latitude_longitude = writer.toString().replaceAll(/[^\d]/, " ")
        latitude_longitude = (latitude_longitude =~ /([0-9]+\s{1,2}){3}/)
        convertCoordinatesToDecimal(latitude_longitude[1][0].tokenize(), latitude_longitude[0][0].tokenize())
      }
      catch(FileNotFoundException ex){
        [latitude : 0,longitude: 0]
      }
    }

}
