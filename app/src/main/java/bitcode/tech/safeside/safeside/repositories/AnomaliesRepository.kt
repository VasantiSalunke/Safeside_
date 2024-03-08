package bitcode.tech.safeside.safeside.repositories

import bitcode.tech.safeside.safeside.ApiResponse
import bitcode.tech.safeside.safeside.models.Anomaly
import bitcode.tech.safeside.safeside.network.AnomaliesApiService

class AnomaliesRepository (
    private val anomaliesApiService: AnomaliesApiService
) : Repository {


    suspend fun getAnomalies(
        lat: Double,
        lng: Double,
        pageNo: Int,
        pageSize: Int
    ): ApiResponse.Success<ArrayList<Anomaly>> {

        return anomaliesApiService.getAnomalies(lat, lng, pageNo, pageSize)

    }
}