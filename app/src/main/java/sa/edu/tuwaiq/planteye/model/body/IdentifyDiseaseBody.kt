package sa.edu.tuwaiq.planteye.model.body


import com.google.gson.annotations.SerializedName

data class IdentifyDiseaseBody(
    @SerializedName("images")
    val images: List<String>,
    @SerializedName("disease_details")
    val diseaseDetails: List<String> = listOf("classification", "common_names", "url", "description"),
    @SerializedName("modifiers")
    val modifiers: List<String> = listOf("health_all"),
    @SerializedName("plant_details")
    val plantDetails: List<String> = listOf("common_names", "url", "wiki_description", "taxonomy")
)