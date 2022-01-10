package cat.copernic.meetdis

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class IniciViewModel : ViewModel() {

        private val _nom = MutableLiveData<String>()
        val nom: LiveData<String>
            get() = _nom

        private val _descripcio = MutableLiveData<String>()
        val descripcio: LiveData<String>
            get() = _descripcio

        private val _foto = MutableLiveData<String>()
        val foto: LiveData<String>
        get() = _foto




}