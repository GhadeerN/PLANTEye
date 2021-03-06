package sa.edu.tuwaiq.planteye.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import sa.edu.tuwaiq.planteye.model.DiagnosesDataModel
import sa.edu.tuwaiq.planteye.model.PlantDataModel
import sa.edu.tuwaiq.planteye.model.body.IdentifyDiseaseBody
import sa.edu.tuwaiq.planteye.model.body.IdentifyPlantBody

/* Plant.id API is an api that use machine learning to identify plant species and diagnose their disease */
interface IPlantIdApi {

    // This is the common header for the api request below it
    @Headers("Api-key: UiPOzaSPxpoKlU8pNoagp03yLAGEdc7pj4lfWB7Xm7Hs1EDGJT")

    // This POST request will send the image (base64)  to the api to identify it
    @POST("/v2/identify")
    suspend fun identifyPlant(
        @Body identifyBody: IdentifyPlantBody
    ): Response<PlantDataModel>

    // The POST request will sent an image with other modifiers to diagnose the plant disease
    @POST("/v2/identify")
    suspend fun identifyPlantDisease(
        @Body identifyDiseaseBody: IdentifyDiseaseBody,
        @Header("Api-key") apiKey: String = "UiPOzaSPxpoKlU8pNoagp03yLAGEdc7pj4lfWB7Xm7Hs1EDGJT"
    ): Response<DiagnosesDataModel>
}