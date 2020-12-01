package nl.appt.model

import android.content.Context
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Jan Jaap de Groot on 27/10/2020
 * Copyright 2020 Stichting Appt
 */
open class Article(
    val type: Article.Type,
    val id: Int,
    val date: String,
    val modified: String?,
    val title: Content,
    val content: Content?,
    val author: Int?,
    val tags: List<Int>?,
    val categories: List<Int>?,
    val link: String?
) : Item, Serializable {

    enum class Type {
        @SerializedName("page")
        PAGE,
        @SerializedName("post")
        POST;

        val path: String
            get() {
                return when (this) {
                    PAGE -> "pages"
                    POST -> "posts"
                }
            }
    }

    override fun title(context: Context) = title.decoded()
}

typealias Page = Article
typealias Post = Article