package sa.edu.tuwaiq.planteye.view.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sa.edu.tuwaiq.planteye.model.PlantDataModel

class PlantDetailsPageViewModel: ViewModel() {
    val plantInfo = MutableLiveData<PlantDataModel>()

}