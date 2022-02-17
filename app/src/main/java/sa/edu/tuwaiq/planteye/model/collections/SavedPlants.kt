package sa.edu.tuwaiq.planteye.model.collections

import sa.edu.tuwaiq.planteye.model.PlantDataModel

data class SavedPlants(
    val plant: PlantDataModel? = null,
//    val userId: String = "",
    var note: String = ""
)