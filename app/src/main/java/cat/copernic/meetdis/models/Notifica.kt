package cat.copernic.meetdis.models

import android.util.Log

data class Notifica(
    val nom: String,
    val img: String
) {


    var nomNotifica: String? = null
    var imageNotifica: String? = null


    init {

        this.nomNotifica = nom
        this.imageNotifica = img

        Log.i("proba_id", "$img")
        Log.i("proba_id", "$imageNotifica")
    }
}
