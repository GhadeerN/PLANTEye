package sa.edu.tuwaiq.planteye.model


import com.google.gson.annotations.SerializedName

data class Taxonomy(
    @SerializedName("class")
    val classX: String? = null,
    @SerializedName("family")
    val family: String? = null,
    @SerializedName("genus")
    val genus: String? = null,
    @SerializedName("kingdom")
    val kingdom: String? = null,
    @SerializedName("order")
    val order: String? = null,
    @SerializedName("phylum")
    val phylum: String? = null
)