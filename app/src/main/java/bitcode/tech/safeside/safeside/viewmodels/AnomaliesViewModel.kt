package bitcode.tech.safeside.safeside.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bitcode.tech.safeside.safeside.models.Anomaly
import bitcode.tech.safeside.safeside.repositories.AnomaliesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnomaliesViewModel(

    private val anomaliesRepository: AnomaliesRepository
) : ViewModel (){

    val anomaliesList = ArrayList<Anomaly>()
    val anomaliesLoadedLiveData = MutableLiveData<Boolean>()
    private var lat = 0
    private var lng = 0
    private var pageNo = 0
    private val pageSize = 50

    fun getAnomalies(){
        CoroutineScope(Dispatchers.IO).launch {
            val response = anomaliesRepository.getAnomalies(lat.toDouble(),
                lng.toDouble(), ++pageNo, pageSize )

            withContext(Dispatchers.Main){
                anomaliesList.addAll(response.data)
                anomaliesLoadedLiveData.postValue(true)
            }
        }

    }



}