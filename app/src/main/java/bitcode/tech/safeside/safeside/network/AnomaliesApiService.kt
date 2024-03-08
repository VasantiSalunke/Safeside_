package bitcode.tech.safeside.safeside.network

import bitcode.tech.safeside.safeside.ApiResponse
import bitcode.tech.safeside.safeside.Commons
import bitcode.tech.safeside.safeside.models.Anomaly
import bitcode.tech.safeside.safeside.models.AnomalyDetails
import bitcode.tech.safeside.safeside.models.ReportAnomaly
import bitcode.tech.safeside.safeside.models.AnomalyReport
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AnomaliesApiService {

    @GET("anomalies/{lat}/{lng}/{pageNo}/{pageSize}")
    suspend fun getAnomalies(
        @Path("lat") lat: Double,
        @Path("lng") lng: Double,
        @Path("pageNo") pageNo: Int,
        @Path("pageSize") pageSize: Int
    ) : ApiResponse.Success<ArrayList<Anomaly>>


    @GET("anomalies/{anomalyId}")
    suspend fun getAnomalyDetails(
        @Path("anomalyId") anomalyId: Int
    ): ApiResponse.Success<AnomalyDetails>

    @POST("anomalies")
    suspend fun addAnomaly(
        @Body reportAnomaly: ReportAnomaly
    ): AnomalyReport


    companion object {
        var anomaliesApiService : AnomaliesApiService? = null


        fun getInstance() : AnomaliesApiService {

            if (anomaliesApiService == null) {
                val httpLogger = HttpLoggingInterceptor()
                httpLogger.level = HttpLoggingInterceptor.Level.BODY

                val client = OkHttpClient.Builder()
                    .addInterceptor(httpLogger)
                    .build()



                anomaliesApiService =
                    Retrofit.Builder()
                        .client(client)
                        .baseUrl(Commons.API_BASE_URL)
                        .addConverterFactory(
                            GsonConverterFactory.create()
                        ).build().create(AnomaliesApiService::class.java)
            }

            return anomaliesApiService!!

        }
    }
}