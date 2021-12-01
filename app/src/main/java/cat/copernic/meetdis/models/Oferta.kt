package cat.copernic.meetdis.models

data class Oferta(val titol: String, val descripcio: String, val dni: String, val data: String, val tipus: String) {
    var titolOferta: String? = null
    var descripcioOferta: String? = null
    var dniOferta: String? = null
    var dataOferta: String? = null
    var tipusOferta: String? = null

    init {
        this.titolOferta = titol
        this.descripcioOferta = descripcio
        this.dniOferta = dni
        this.dataOferta = data
        this.tipusOferta = tipus
    }

}
