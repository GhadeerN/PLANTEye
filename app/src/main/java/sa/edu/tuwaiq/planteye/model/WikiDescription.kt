package sa.edu.tuwaiq.planteye.model


import com.google.gson.annotations.SerializedName

data class WikiDescription(
    @SerializedName("citation")
    val citation: String? = null,
    @SerializedName("license_name")
    val licenseName: String? = null,
    @SerializedName("license_url")
    val licenseUrl: String? = null,
    @SerializedName("value")
    val value: String? = null
)