package sa.edu.tuwaiq.planteye.model


import com.google.gson.annotations.SerializedName

data class Disease(
    @SerializedName("classification")
    val classification: List<String>,
    @SerializedName("disease_details")
    val diseaseDetails: DiseaseDetails,
    @SerializedName("entity_id")
    val entityId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("probability")
    val probability: Double
)