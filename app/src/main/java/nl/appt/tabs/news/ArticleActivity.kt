package nl.appt.tabs.news

import android.net.Uri
import androidx.core.app.ShareCompat
import nl.appt.R
import nl.appt.api.API
import nl.appt.api.Response
import nl.appt.extensions.*
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

    private val id: Int?
        get() = intent.getId()

    private val slug: String?
        get() = intent.getSlug()

    private val uri: Uri?
        get() = intent.getUri()

    private val title: String?
        get() = intent.getTitle()

    override fun getLayoutId() = R.layout.activity_web

    override fun getToolbarTitle(): String? = title

    override fun onViewCreated() {
        super.onViewCreated()

        article?.let {
            onArticle(it)
        } ?: run {
            isLoading = true

            val callback = { response: Response<Article> ->
                onResponse(response)
            }

            id?.let { id ->
                API.getArticle(type, id, callback)
            }
            slug?.let { slug ->
                API.getArticle(type, slug, callback)
            }
            uri?.let { uri ->
                API.getArticle(type, uri, callback)
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