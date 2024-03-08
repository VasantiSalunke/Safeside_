package bitcode.tech.safeside.safeside.models

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.lang.reflect.Type

@JsonAdapter(AnomalyDeserializer::class)

data class Anomaly(
    @SerializedName("anomaly_id")
    val id: Int,

    @SerializedName("anomaly_title")
    val title: String,

    @SerializedName("unsafe_rating")
    val safetyrating: String,

    @SerializedName("lat")
    val lat: Double,

    @SerializedName("lng")
    val lng: Double
) : Serializable

class AnomalyDeserializer : JsonDeserializer<Anomaly> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Anomaly {
        val jsonObject = json?.asJsonObject

        val id = jsonObject?.get("anomaly_id")!!.asInt
        val title = jsonObject.get("anomaly_title")!!.asString
        val safetyrating = jsonObject.get("unsafe_rating")!!.asString
        val lat = jsonObject.get("lat")!!.asDouble
        val lng = jsonObject.get("lng")!!.asDouble

        return Anomaly(
            id,
            title,
            safetyrating,
            lat,
            lng
        )
    }

}