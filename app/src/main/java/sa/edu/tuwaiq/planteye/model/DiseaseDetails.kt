package sa.edu.tuwaiq.planteye.model


import com.google.gson.annotations.SerializedName

data class DiseaseDetails(
    @SerializedName("classification")
    val classification: List<String>? = null,
    @SerializedName("common_names")
    val commonNames: List<String>? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("url")
    val url: String? = null
)