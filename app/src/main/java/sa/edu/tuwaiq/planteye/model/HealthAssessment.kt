package sa.edu.tuwaiq.planteye.model


import com.google.gson.annotations.SerializedName

data class HealthAssessment(
    @SerializedName("diseases")
    val diseases: List<Disease>,
    @SerializedName("diseases_simple")
    val diseasesSimple: List<DiseasesSimple>,
    @SerializedName("is_healthy")
    val isHealthy: Boolean,
    @SerializedName("is_healthy_probability")
    val isHealthyProbability: Double
)