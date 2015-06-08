package com.ambienta2mx.fasteagle

/**
 * Created by alberto on 4/16/15.
 */
class Place {
    def location //GEOJson Spec
    List sexagesimal_coordinates
    List itrf_coordinates
    List nad27_coordinates
    def height
    String town
    String state
    String city
    String fullName
    String zipCode
    List extraInfo
    List provider
    Date lastUpdated
    Date dateCreated
}
