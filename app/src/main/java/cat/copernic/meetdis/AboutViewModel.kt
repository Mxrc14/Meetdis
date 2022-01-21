package cat.copernic.meetdis

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.provider.Settings
import androidx.databinding.ObservableInt
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.grpc.Contexts


class AboutViewModel(application: Application) : AndroidViewModel(application) {


    private val _text3 = MutableLiveData<String>()
    val text3: LiveData<String>
        get() = _text3

    private val _text4 = MutableLiveData<String>()
    val text4: LiveData<String>
        get() = _text4

    private val _text5 = MutableLiveData<String>()
    val text5: LiveData<String>
        get() = _text5

    init{


        _text3.value = application.applicationContext.getString(R.string.aboutApp)
        _text4.value = application.applicationContext.getString(R.string.propietaris)
        _text5.value = application.applicationContext.getString(R.string.versio)

    }




}
