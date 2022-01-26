package cat.copernic.meetdis

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ContacteViewModel (application: Application) : AndroidViewModel(application) {


    private val _nomICognom = MutableLiveData<String>()
    val nomICognom: LiveData<String>
        get() = _nomICognom

    private val _titol = MutableLiveData<String>()
    val titol: LiveData<String>
        get() = _titol

    private val _descripcio = MutableLiveData<String>()
    val descripcio: LiveData<String>
        get() = _descripcio

    private val _enviar = MutableLiveData<String>()
    val enviar: LiveData<String>
        get() = _enviar

    init{

        _nomICognom.value = application.applicationContext.getString(R.string.nomICognoms)
        _titol.value = application.applicationContext.getString(R.string.titol)
        _descripcio.value = application.applicationContext.getString(R.string.descripcio)
        _enviar.value = application.applicationContext.getString(R.string.enviar)

    }

}
