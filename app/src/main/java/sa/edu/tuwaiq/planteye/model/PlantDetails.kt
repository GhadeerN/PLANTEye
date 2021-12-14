package sa.edu.tuwaiq.planteye.model


import com.google.gson.annotations.SerializedName

data class PlantDetails(
    @SerializedName("common_names")
    val commonNames: List<String>? = null,
    @SerializedName("scientific_name")
    val scientificName: String? = null,
    @SerializedName("structured_name")
    val structuredName: StructuredName? = null,
    @SerializedName("taxonomy")
    val taxonomy: Taxonomy? = null,
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("wiki_description")
    val wikiDescription: WikiDescription? = null
)