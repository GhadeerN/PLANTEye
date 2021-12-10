package sa.edu.tuwaiq.planteye.model


import com.google.gson.annotations.SerializedName

data class StructuredName(
    @SerializedName("genus")
    val genus: String,
    @SerializedName("species")
    val species: String
)