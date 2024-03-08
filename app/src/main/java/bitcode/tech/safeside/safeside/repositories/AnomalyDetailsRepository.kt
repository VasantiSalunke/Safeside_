package bitcode.tech.safeside.safeside.repositories

import bitcode.tech.safeside.safeside.ApiResponse
import bitcode.tech.safeside.safeside.models.AnomalyDetails
import bitcode.tech.safeside.safeside.network.AnomaliesApiService.Companion.anomaliesApiService

class AnomalyDetailsRepository(): Repository {

        suspend fun getAnomalyDetails(
            anomalyId : Int
        ): ApiResponse.Success<AnomalyDetails>? {

            return anomaliesApiService?.getAnomalyDetails(anomalyId)

        }
}