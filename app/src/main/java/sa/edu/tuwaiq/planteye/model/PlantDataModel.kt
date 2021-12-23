package sa.edu.tuwaiq.planteye.model

import com.google.gson.annotations.SerializedName

data class PlantDataModel(
    @SerializedName("countable")
    val countable: Boolean? = null,
    @SerializedName("custom_id")
    val customId: Any? = null,
    @SerializedName("fail_cause")
    val failCause: Any? = null,
    @SerializedName("feedback")
    val feedback: Any? = null,
    @SerializedName("finished_datetime")
    val finishedDatetime: Double? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("images")
    val images: List<Image>? = null,
    @SerializedName("is_plant")
    val isPlant: Boolean? = null,
    @SerializedName("is_plant_probability")
    val isPlantProbability: Double? = null,
    @SerializedName("meta_data")
    val metaData: MetaData? = null,
    @SerializedName("modifiers")
    val modifiers: List<Any>? = null,
    @SerializedName("secret")
    val secret: String? = null,
    @SerializedName("suggestions")
    val suggestions: List<Suggestion>? = null,
    @SerializedName("uploaded_datetime")
    val uploadedDatetime: Double? = null,

    // User note
//    var note: String = ""
)
//{
//    // I override this function (Why?) - so the note update operation work properly
//    override fun equals(obj: Any?): Boolean {
//        return this.id == (obj as PlantDataModel).id
//    }
//}