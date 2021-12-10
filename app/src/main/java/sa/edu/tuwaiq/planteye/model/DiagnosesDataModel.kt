package sa.edu.tuwaiq.planteye.model


import com.google.gson.annotations.SerializedName

data class DiagnosesDataModel(
    @SerializedName("countable")
    val countable: Boolean,
    @SerializedName("custom_id")
    val customId: Any,
    @SerializedName("fail_cause")
    val failCause: Any,
    @SerializedName("feedback")
    val feedback: Any,
    @SerializedName("finished_datetime")
    val finishedDatetime: Double,
    @SerializedName("health_assessment")
    val healthAssessment: HealthAssessment,
    @SerializedName("id")
    val id: Int,
    @SerializedName("images")
    val images: List<Image>,
    @SerializedName("is_plant")
    val isPlant: Boolean,
    @SerializedName("is_plant_probability")
    val isPlantProbability: Double,
    @SerializedName("meta_data")
    val metaData: MetaData,
    @SerializedName("modifiers")
    val modifiers: List<String>,
    @SerializedName("secret")
    val secret: String,
    @SerializedName("suggestions")
    val suggestions: List<Suggestion>,
    @SerializedName("uploaded_datetime")
    val uploadedDatetime: Double
)