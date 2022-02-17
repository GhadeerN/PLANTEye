package sa.edu.tuwaiq.planteye.model.collections

import sa.edu.tuwaiq.planteye.model.PlantDataModel

data class User(
    val fullName: String = "",
    val email: String = "",
    var savedPlants: MutableList<PlantDataModel> = mutableListOf()
)
