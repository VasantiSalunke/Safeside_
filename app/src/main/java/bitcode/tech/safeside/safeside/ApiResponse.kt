package bitcode.tech.safeside.safeside


sealed class ApiResponse <T : Any>{

    data class Success<T : Any>(
        val responseCode: Int,
        val message : String,
        val data : T
    ) : ApiResponse<T>()

    data class Failure(val error: Error): ApiResponse<Nothing>()

    data class NoInternet(val error: Error):ApiResponse<Nothing>()

    data class Error(val responseCode: Int,  val throwable: Throwable?)
}