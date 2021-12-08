package cat.copernic.meetdis.models

data class Membre(
    val cognoms: String,
    val contrasenya: String,
    val dni: String,
    val nom: String,
    val tipus: String) {


    var nomMembre: String? = null
    var cognomsMembre: String? = null
    var dniOferta: String? = null
    var contrasenyaMembre: String? = null
    var tipusMembre: String? = null

    init {
        this.nomMembre = nom
        this.cognomsMembre = cognoms
        this.dniOferta = dni
        this.contrasenyaMembre = contrasenya
        this.tipusMembre = tipus
    }

}
