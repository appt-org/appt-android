package nl.appt.tabs.knowledge

import androidx.core.app.ShareCompat
import nl.appt.R
import nl.appt.api.API
import nl.appt.api.Response
import nl.appt.extensions.getArticleType
import nl.appt.extensions.getId
import nl.appt.extensions.getSlug
import nl.appt.extensions.showError
import nl.appt.model.Article
import nl.appt.widgets.WebActivity

/**
 * Created by Jan Jaap de Groot on 28/10/2020
 * Copyright 2020 Stichting Appt
 */
class ArticleActivity: WebActivity() {

    private var article: Article? = null

    private val type: Article.Type
        get() = intent.getArticleType()

    private val id: Int
        get() = intent.getId()

    private val slug: String?
        get() = intent.getSlug()

    override fun getLayoutId() = R.layout.activity_web

    override fun getToolbarTitle(): String? = null

    override fun onViewCreated() {
        super.onViewCreated()

        article?.let {
            onArticle(it)
        } ?: run {
            isLoading = true

            slug?.let { slug ->
                API.getArticle(type, slug) {
                    onResponse(it)
                }
            } ?: run {
                API.getArticle(type, id) {
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
            isLoading = false
            showError(error) {
                finish()
            }
        }
    }

    private fun onArticle(article: Article) {
        this.article = article

        article.content?.let { content ->
            setShareEnabled(true)
            load(content = content.rendered, title = article.title.rendered)
        }
    }

    override fun onShare() {
        article?.let { article ->
            ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setChooserTitle(R.string.action_share_article)
                .setSubject(article.title.decoded())
                .setText(article.link)
                .startChooser()
        }
    }
}