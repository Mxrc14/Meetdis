package cat.copernic.meetdis

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class aboutViewModel: ViewModel() {
    val aboutModel = MutableLiveData<aboutModel>()

    fun getText(){
        val currentText : aboutModel = aboutProvider.getAbout()
        aboutModel.postValue(currentText)
    }
}
