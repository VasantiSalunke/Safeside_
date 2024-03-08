package bitcode.tech.safeside.safeside.repositories


import bitcode.tech.safeside.safeside.models.ReportAnomaly
import bitcode.tech.safeside.safeside.models.AnomalyReport
import bitcode.tech.safeside.safeside.network.AnomaliesApiService.Companion.anomaliesApiService


class ReportAnomalyRepository(

) : Repository {

    suspend fun addAnomaly(reportAnomaly: ReportAnomaly)
            : AnomalyReport {

        return anomaliesApiService!!.addAnomaly(reportAnomaly)


    }
}