package mx.ipn.ambienta2mx.fastEagle.enums

enum StateCode {
    BC("Baja California"),
    SO("Sonora"),
    CH("Chihuahua"),
    CL("Coahuila"),
    NL("Nuevo León"),
    TM("Tamaulipas"),
    BS("Baja California Sur"),
    DG("Durango"),
    ZC("Zacatecas"),
    AG("Aguascalientes"),
    SL("San Luis Potosí"),
    JA("Jalisco"),
    CO("Colima"),
    GT("Guanajuato"),
    QO("Querétaro"),
    MC("Michoacán"),
    MX("Edo. de México"),
    DF("Distrito Federal"),
    HI("Hidalgo"),
    VR("Veracruz"),
    GR("Guerrero"),
    MO("Morelos"),
    TL("Tlaxcala"),
    PU("Puebla"),
    OX("Oaxaca"),
    CS("Chiapas"),
    TB("Tabasco"),
    CM("Campeche"),
    YC("Yucatán"),
    QR("Quintana Roo"),
    SI("Sinaloa"),
    NY("Nayarit")

    final String value

    StateCode(String value) { this.value = value }

    String toString() { value }

    String getKey() { name() }

}
