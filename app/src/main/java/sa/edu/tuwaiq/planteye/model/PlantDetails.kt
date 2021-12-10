package sa.edu.tuwaiq.planteye.model


import com.google.gson.annotations.SerializedName

data class PlantDetails(
    @SerializedName("common_names")
    val commonNames: List<String>,
    @SerializedName("scientific_name")
    val scientificName: String,
    @SerializedName("structured_name")
    val structuredName: StructuredName,
    @SerializedName("taxonomy")
    val taxonomy: Taxonomy,
    @SerializedName("url")
    val url: String,
    @SerializedName("wiki_description")
    val wikiDescription: WikiDescription
)