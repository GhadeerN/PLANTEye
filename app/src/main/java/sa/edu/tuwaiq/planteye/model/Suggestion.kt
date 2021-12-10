package sa.edu.tuwaiq.planteye.model


import com.google.gson.annotations.SerializedName

data class Suggestion(
    @SerializedName("confirmed")
    val confirmed: Boolean,
    @SerializedName("id")
    val id: Int,
    @SerializedName("plant_details")
    val plantDetails: PlantDetails,
    @SerializedName("plant_name")
    val plantName: String,
    @SerializedName("probability")
    val probability: Double
)