package nl.appt.tabs.knowledge

import android.annotation.SuppressLint
import nl.appt.R
import nl.appt.api.API
import nl.appt.api.Response
import nl.appt.model.Article
import nl.appt.widgets.WebActivity

/**
 * Created by Jan Jaap de Groot on 28/10/2020
 * Copyright 2020 Stichting Appt
 */
@SuppressLint("SetJavaScriptEnabled")
class ArticleActivity: WebActivity() {

    private var article: Article? = null

    private val id: Int
        get() = intent.getIntExtra("id", -1)

    private val slug: String?
        get() = intent.getStringExtra("slug")

    override fun getViewId(): Int {
        return R.layout.activity_web
    }

    override fun getToolbarTitle(): String? {
        return null
    }

    override fun onViewCreated() {
        super.onViewCreated()

        article?.let {
            onArticle(it)
        } ?: run {
            setLoading(true)

            slug?.let { slug ->
                API.getArticle(slug) {
                    onResponse(it)
                }
            } ?: run {
                API.getArticle(id) {
                    onResponse(it)
                }
            }
        }
    }

    private fun onResponse(response: Response<Article>) {
        response.result?.let { article ->
            onArticle(article)
        }

        response.error?.let { error ->
            setLoading(false)
            // TODO: Show error
        }
    }

    private fun onArticle(article: Article) {
        this.article = article

        article.content?.let { content ->
            load(content = content.rendered, title = article.title.rendered)
        }
    }
}