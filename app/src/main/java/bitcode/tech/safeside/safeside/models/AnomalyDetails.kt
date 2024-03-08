package bitcode.tech.safeside.safeside.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

//@JsonAdapter(AnomalyDetailsDeserializer :: class)

data class AnomalyDetails(
   @SerializedName("anomaly_id")
    val id: Int,
    val title: String,
    val description: String,
    val added_on : String,
    val unsafe_rating : Int,
    val lat : Double,
    val lng : Double,
    @SerializedName("images")
    val imageurls : ArrayList<String>

) : Serializable

/*
class AnomalyDetailsDeserializer : JsonDeserializer<AnomalyDetails>{

   override fun deserialize(
      json: JsonElement?,
      typeOfT: Type?,
      context: JsonDeserializationContext?
   ): AnomalyDetails {


      val jsonObject = json?.asJsonObject!!

      return AnomalyDetails(
         jsonObject.get("id").asInt,
         jsonObject.get("title").asString,
         jsonObject.get("description").asString,
         jsonObject.get("added_on").asString,
         jsonObject.get("unsafe_rating").asInt,
         jsonObject.get("lat").asDouble,
         jsonObject.get("lng").asDouble,
         jsonObject.get("imageurl").asJsonArray[0].asJsonObject.get("path").asString

      )
   }
}*/
