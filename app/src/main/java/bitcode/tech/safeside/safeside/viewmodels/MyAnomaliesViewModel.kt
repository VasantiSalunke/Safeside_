package bitcode.tech.safeside.safeside.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bitcode.tech.safeside.safeside.models.MyAnomaly
import bitcode.tech.safeside.safeside.repositories.MyAnomaliesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyAnomaliesViewModel(
    private val myAnomaliesRepository: MyAnomaliesRepository
) : ViewModel(){

    val myAnomaliesList = ArrayList<MyAnomaly>()
    val myAnomaliesLoadedLiveData = MutableLiveData<Boolean>()

    fun getMyAnomalies(){

        CoroutineScope(Dispatchers.IO).launch {
            val myAnomalies  = myAnomaliesRepository.getMyAnomalies()

            withContext(Dispatchers.Main){
                myAnomaliesList.addAll(myAnomalies)
                myAnomaliesLoadedLiveData.postValue(true)
            }
        }
    }
}