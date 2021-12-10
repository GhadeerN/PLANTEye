package sa.edu.tuwaiq.planteye.model


import com.google.gson.annotations.SerializedName

data class DiseaseDetails(
    @SerializedName("classification")
    val classification: List<String>,
    @SerializedName("common_names")
    val commonNames: List<String>,
    @SerializedName("description")
    val description: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)