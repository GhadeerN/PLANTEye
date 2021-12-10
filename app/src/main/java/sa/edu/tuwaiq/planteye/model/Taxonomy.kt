package sa.edu.tuwaiq.planteye.model


import com.google.gson.annotations.SerializedName

data class Taxonomy(
    @SerializedName("class")
    val classX: String,
    @SerializedName("family")
    val family: String,
    @SerializedName("genus")
    val genus: String,
    @SerializedName("kingdom")
    val kingdom: String,
    @SerializedName("order")
    val order: String,
    @SerializedName("phylum")
    val phylum: String
)