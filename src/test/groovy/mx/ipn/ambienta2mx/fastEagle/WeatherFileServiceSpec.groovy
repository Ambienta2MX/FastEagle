package mx.ipn.ambienta2mx.fastEagle
import spock.lang.Specification
import spock.lang.Shared
import java.lang.Void as Should
import mx.ipn.ambienta2mx.fastEagle.enums.StateCode
import mx.ipn.ambienta2mx.fastEagle.services.WeatherFileService

class WeatherFileServiceSpec extends Specification{
  
  @Shared service = new WeatherFileService()

  Should "get the urls of each station"(){ 
    given:"the state code"
      def stateCode = StateCode.DF
    when:
      def urls = service.getFileUrlsForStation(stateCode)
    then:
      urls.size() == 63  
      urls.first().startsWith("http://smn.cna.gob.mx")
  }
  
  Should "get the urls of the country"(){
    when:
      def countryUrls = service.getFileUrlsOfCountry()
    then:
      countryUrls.first().latitude.class.simpleName == "BigDecimal"
      countryUrls.first().url.class.simpleName == "String"
  }

  Should "get the decimal coordinates from degrees to decimal"(){
    given:"the longitude with minutes and seconds"
      def latitude = ["28","53","47"]
      def longitude = ["113","33","37"] 
    when:
      def decimalCoordinates = service.convertCoordinatesToDecimal(latitude,longitude)
    then:
      decimalCoordinates.latitude == 28.8964
      decimalCoordinates.longitude == -113.5603
  }
  
  Should "get the latitude and longitude of the file"(){
    given:"the url"
      def fileUrl = "http://smn.cna.gob.mx/emas/txt/BC05_10M.TXT"
    when:
      def coordinates= service.getUrlCoordinates(fileUrl)
    then:
      coordinates.latitude == 28.8964
      coordinates.longitude == -113.5603
  }

}
