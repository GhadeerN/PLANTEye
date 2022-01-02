package sa.edu.tuwaiq.planteye.model


import com.google.gson.annotations.SerializedName

data class Disease(
    @SerializedName("classification")
    val classification: List<String>? = null,
    @SerializedName("disease_details")
    val diseaseDetails: DiseaseDetails? = null,
    @SerializedName("entity_id")
    val entityId: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("probability")
    val probability: Double? = null
)