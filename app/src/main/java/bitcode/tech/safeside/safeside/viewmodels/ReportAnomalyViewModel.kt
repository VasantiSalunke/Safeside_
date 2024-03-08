package bitcode.tech.safeside.safeside.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bitcode.tech.safeside.safeside.models.ReportAnomaly
import bitcode.tech.safeside.safeside.models.AnomalyReport
import bitcode.tech.safeside.safeside.repositories.ReportAnomalyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReportAnomalyViewModel(
 private val repository: ReportAnomalyRepository
) : ViewModel() {

    val reportPostedLiveData = MutableLiveData<AnomalyReport>()

    fun addAnomaly(reportAnomaly: ReportAnomaly) {
        CoroutineScope(Dispatchers.IO).launch {

            try {
                val responsePostAnomaly = repository.addAnomaly(reportAnomaly)

                withContext(Dispatchers.Main) {
                    reportPostedLiveData.postValue(responsePostAnomaly)
                }
            } catch (e: Exception) {
                Log.e("tag", "Error: ${e.message}", e)
                reportPostedLiveData.postValue(AnomalyReport(0, "false"))
                e.printStackTrace()

          }

            }

        }

    }
