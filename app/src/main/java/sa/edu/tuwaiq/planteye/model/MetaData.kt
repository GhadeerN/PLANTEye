package sa.edu.tuwaiq.planteye.model


import com.google.gson.annotations.SerializedName

data class MetaData(
    @SerializedName("date")
    val date: String,
    @SerializedName("datetime")
    val datetime: String,
    @SerializedName("latitude")
    val latitude: Any,
    @SerializedName("longitude")
    val longitude: Any
)