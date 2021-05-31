package nl.appt.extensions

import android.content.Intent
import android.net.Uri
import nl.appt.model.Action
import nl.appt.model.Article
import nl.appt.model.Filters
import nl.appt.model.Gesture
import nl.appt.model.Block

/**
 * Created by Jan Jaap de Groot on 06/11/2020
 * Copyright 2020 Stichting Appt
 */

/** Block **/
private const val KEY_BLOCK = "block"
fun Intent.getBlock() = getParcelableExtra<Block>(KEY_BLOCK) as Block
fun Intent.setBlock(block: Block) = putExtra(KEY_BLOCK, block)

/** Gesture **/
private const val KEY_GESTURE = "gesture"
fun Intent.getGesture() = getSerializableExtra(KEY_GESTURE) as? Gesture
fun Intent.setGesture(gesture: Gesture) = putExtra(KEY_GESTURE, gesture)

/** Gestures **/
private const val KEY_GESTURES = "gestures"
fun Intent.getGestures() = getSerializableExtra(KEY_GESTURES) as? ArrayList<Gesture>
fun Intent.setGestures(gestures: ArrayList<Gesture>) = putExtra(KEY_GESTURES, gestures)

/** Launch **/
private const val KEY_LAUNCH = "launch"
fun Intent.getLaunch() = getBooleanExtra(KEY_LAUNCH, false)
fun Intent.setLaunch(launch: Boolean) = putExtra(KEY_LAUNCH, launch)

/** Type **/
private const val KEY_TYPE = "type"
fun Intent.getArticleType() = getSerializableExtra(KEY_TYPE) as Article.Type
fun Intent.setArticleType(type: Article.Type) = putExtra(KEY_TYPE, type)

/** Id **/
private const val KEY_ID = "id"
fun Intent.getId(): Int? {
    val id = getIntExtra(KEY_ID, -1)
    return when (id != -1) {
        true -> id
        false -> null
    }
}
fun Intent.setId(id: Int) = putExtra(KEY_ID, id)

/** Slug **/
private const val KEY_SLUG = "slug"
fun Intent.getSlug() = getStringExtra(KEY_SLUG)
fun Intent.setSlug(slug: String) = putExtra(KEY_SLUG, slug)

/** Uri **/
private const val KEY_URI = "uri"
fun Intent.getUri() = getParcelableExtra(KEY_URI) as? Uri
fun Intent.setUri(uri: Uri) = putExtra(KEY_URI, uri)

/** Filters **/
private const val KEY_FILTERS = "filters"
fun Intent.getFilters() = getSerializableExtra(KEY_FILTERS) as? Filters
fun Intent.setFilters(filters: Filters?) = putExtra(KEY_FILTERS, filters)

/** Title **/
private const val KEY_TITLE = "title"
fun Intent.getTitle() = getStringExtra(KEY_TITLE)
fun Intent.setTitle(title: String) = putExtra(KEY_TITLE, title)

/** Text **/
private const val KEY_TEXT = "text"
fun Intent.getText() = getStringExtra(KEY_TEXT)
fun Intent.setText(text: String) = putExtra(KEY_TEXT, text)

/** Action **/
private const val KEY_ACTION = "action"
fun Intent.getAction2() = getSerializableExtra(KEY_ACTION) as? Action
fun Intent.setAction2(action: Action) = putExtra(KEY_ACTION, action)