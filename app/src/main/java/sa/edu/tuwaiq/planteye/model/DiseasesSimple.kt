package sa.edu.tuwaiq.planteye.model


import com.google.gson.annotations.SerializedName

data class DiseasesSimple(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("probability")
    val probability: Double? = null
)