package cat.copernic.meetdis.models

import android.util.Log

data class Membre(
    val cognoms: String,
    val contrasenya: String,
    val dni: String,
    val nom: String,
    val tipus: String,
    val img: String
    ) {


    var nomMembre: String? = null
    var cognomsMembre: String? = null
    var dniMembre: String? = null
    var contrasenyaMembre: String? = null
    var tipusMembre: String? = null
    var imageMembre: String? = null


    init {

        this.nomMembre = nom
        this.cognomsMembre = cognoms
        this.dniMembre = dni
        this.contrasenyaMembre = contrasenya
        this.tipusMembre = tipus
        this.imageMembre = img

        Log.i("proba_id", "$img")
        Log.i("proba_id", "$imageMembre")


    }
}
