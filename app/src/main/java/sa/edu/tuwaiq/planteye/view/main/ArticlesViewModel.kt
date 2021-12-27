package sa.edu.tuwaiq.planteye.view.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import sa.edu.tuwaiq.planteye.model.collections.Article
import sa.edu.tuwaiq.planteye.repositories.FirestoreServiceRepository
import java.lang.Exception


private const val TAG = "ArticlesViewModel"
class ArticlesViewModel: ViewModel() {
    private val firebaseRepo = FirestoreServiceRepository.get()

    var articlesLiveData = MutableLiveData<List<Article>>()
    var articlesErrorLiveData = MutableLiveData<String>()
    var selectedArticle = MutableLiveData<Article>()
    var article = mutableListOf<Article>()

    fun callArticles() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = firebaseRepo.getArticles()
                response.addSnapshotListener(object : EventListener<QuerySnapshot> {
                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ) {
                        if (error != null) {
                            articlesErrorLiveData.postValue(error.message)
                            Log.d(TAG, "SNAPSHOT ERROR: ${error.message}")
                            return
                        }

                        for (doc : DocumentChange in value?.documentChanges!!) {
                            if (doc.type == DocumentChange.Type.ADDED) {
                                val articleDoc = doc.document.toObject(Article::class.java)
                                article.add(articleDoc)
//                                articlesLiveData.postValue(userDoc)
                            }
                        }
                        articlesLiveData.postValue(article)
                        article = mutableListOf()
                    }
                })
            } catch (e: Exception) {
                Log.d(TAG, "Catch - ${e.message}")
                articlesErrorLiveData.postValue(e.message)
            }
        }
    }
}