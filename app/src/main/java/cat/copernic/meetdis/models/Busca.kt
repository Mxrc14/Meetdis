package cat.copernic.meetdis.models;

import android.util.Log
data class Busca(val titol:String, val imatge:String) {
    var titolBusca:String?=null
    var imatgeBusca:String?=null

    init {
        this.titolBusca = titol

        this.imatgeBusca = imatge

        Log.i("proba_id", "$imatgeBusca")
        Log.i("proba_id", "$imatge")
    }

}