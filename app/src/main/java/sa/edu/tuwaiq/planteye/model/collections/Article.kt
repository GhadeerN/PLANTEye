package sa.edu.tuwaiq.planteye.model.collections

data class Article(
    val title: String = "",
    val content: String = "",
    val image: String = "",
    val date: String = "",
    val id: Int? = null,
    val url: String = ""
)
