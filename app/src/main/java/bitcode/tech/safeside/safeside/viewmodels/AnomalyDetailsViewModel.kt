package bitcode.tech.safeside.safeside.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bitcode.tech.safeside.safeside.models.AnomalyDetails
import bitcode.tech.safeside.safeside.repositories.AnomalyDetailsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnomalyDetailsViewModel(
private val anomalyDetailsRepository: AnomalyDetailsRepository
) : ViewModel() {

    val anomalyDetailsList = ArrayList<AnomalyDetails>()
    val anomalyDetailsLiveData = MutableLiveData<AnomalyDetails>()


    fun getAnomalyDetails(anomalyId : Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = anomalyDetailsRepository.getAnomalyDetails(anomalyId)

            withContext(Dispatchers.Main) {
                anomalyDetailsLiveData.postValue(response!!.data)
            }
        }
    }
}
