package bitcode.tech.safeside.safeside.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import bitcode.tech.safeside.safeside.repositories.AnomaliesRepository
import bitcode.tech.safeside.safeside.repositories.AnomalyDetailsRepository
import bitcode.tech.safeside.safeside.repositories.MyAnomaliesRepository
import bitcode.tech.safeside.safeside.repositories.ReportAnomalyRepository
import bitcode.tech.safeside.safeside.repositories.Repository
import bitcode.tech.safeside.safeside.viewmodels.AnomaliesViewModel
import bitcode.tech.safeside.safeside.viewmodels.AnomalyDetailsViewModel
import bitcode.tech.safeside.safeside.viewmodels.MyAnomaliesViewModel
import bitcode.tech.safeside.safeside.viewmodels.ReportAnomalyViewModel

class AnomaliesViewModelFactory(
    val repository:Repository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnomaliesViewModel::class.java) && repository is AnomaliesRepository) {
            return AnomaliesViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(MyAnomaliesViewModel::class.java) && repository is MyAnomaliesRepository) {
            return MyAnomaliesViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(AnomalyDetailsViewModel::class.java) && repository is AnomalyDetailsRepository) {
            return AnomalyDetailsViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(ReportAnomalyViewModel::class.java) && repository is ReportAnomalyRepository) {
            return ReportAnomalyViewModel(repository) as T
        }

        throw IllegalStateException("Unable to create View Model")
    }
}
