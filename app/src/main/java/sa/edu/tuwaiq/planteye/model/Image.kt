package sa.edu.tuwaiq.planteye.model


import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("file_name")
    val fileName: String? = null,
    @SerializedName("url")
    val url: String? = null
)