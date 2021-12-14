package sa.edu.tuwaiq.planteye.model


import com.google.gson.annotations.SerializedName

data class Suggestion(
    @SerializedName("confirmed")
    val confirmed: Boolean? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("plant_details")
    val plantDetails: PlantDetails? = null,
    @SerializedName("plant_name")
    val plantName: String? = null,
    @SerializedName("probability")
    val probability: Double? = null
)