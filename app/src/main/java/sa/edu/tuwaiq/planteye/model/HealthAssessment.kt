package sa.edu.tuwaiq.planteye.model


import com.google.gson.annotations.SerializedName

data class HealthAssessment(
    @SerializedName("diseases")
    val diseases: List<Disease>? = null,
    @SerializedName("diseases_simple")
    val diseasesSimple: List<DiseasesSimple>? = null,
    @SerializedName("is_healthy")
    val isHealthy: Boolean? = null,
    @SerializedName("is_healthy_probability")
    val isHealthyProbability: Double? = null
)