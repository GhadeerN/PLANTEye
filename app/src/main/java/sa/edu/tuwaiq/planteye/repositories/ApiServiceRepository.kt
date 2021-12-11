package sa.edu.tuwaiq.planteye.repositories

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sa.edu.tuwaiq.planteye.api.IPlantIdApi
import sa.edu.tuwaiq.planteye.model.body.IdentifyDiseaseBody
import sa.edu.tuwaiq.planteye.model.body.IdentifyPlantBody
import java.lang.Exception

private const val BASE_URL = "https://api.plant.id"

class ApiServiceRepository {

    private val retrofitService = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(
        GsonConverterFactory.create()
    ).build()

    private val retrofitApi = retrofitService.create(IPlantIdApi::class.java)

    //TODO Shared preference maybe here

    // Identify Plant
    suspend fun identifyPlant(identifyPlantBody: IdentifyPlantBody) =
        retrofitApi.identifyPlant(identifyPlantBody)

    // Disease Diagnoses
    suspend fun diagnosePlant(identifyDiseaseBody: IdentifyDiseaseBody) =
        retrofitApi.identifyPlantDisease(identifyDiseaseBody)

    companion object {
        //TODO may need context here!
        private var instance: ApiServiceRepository? = null

        fun init() {
            if (instance == null)
                instance = ApiServiceRepository()
        }

        fun get(): ApiServiceRepository {
            return instance ?: throw Exception("Api service repository must be initialized")
        }
    }
}