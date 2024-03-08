package bitcode.tech.safeside.safeside.models

data class ReportAnomaly(
  val added_on: String,
  val description: String,
  val lat: Double,
  val lng: Double,
  val title: String,
  val unsafe_rating : String
)

