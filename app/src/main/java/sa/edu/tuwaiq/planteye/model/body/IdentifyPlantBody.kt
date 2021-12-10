package sa.edu.tuwaiq.planteye.model.body


import com.google.gson.annotations.SerializedName

data class IdentifyPlantBody(
    @SerializedName("images")
    val images: List<String>,
    @SerializedName("plant_details")
    val plantDetails: List<String> = listOf("common_names", "url", "wiki_description", "taxonomy", "scientific_name")
)