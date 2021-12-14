package sa.edu.tuwaiq.planteye.model


import com.google.gson.annotations.SerializedName

data class MetaData(
    @SerializedName("date")
    val date: String? = null,
    @SerializedName("datetime")
    val datetime: String? = null,
    @SerializedName("latitude")
    val latitude: Any? = null,
    @SerializedName("longitude")
    val longitude: Any? = null
)