package sa.edu.tuwaiq.planteye.model


import com.google.gson.annotations.SerializedName

data class WikiDescription(
    @SerializedName("citation")
    val citation: String,
    @SerializedName("license_name")
    val licenseName: String,
    @SerializedName("license_url")
    val licenseUrl: String,
    @SerializedName("value")
    val value: String
)