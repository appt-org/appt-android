package nl.appt.extensions

import android.content.Intent
import nl.appt.model.Action
import nl.appt.model.Article
import nl.appt.model.Filters
import nl.appt.model.Gesture

/**
 * Created by Jan Jaap de Groot on 06/11/2020
 * Copyright 2020 Stichting Appt
 */

/** Gesture **/
private const val KEY_GESTURE = "gesture"
fun Intent.getGesture() = getSerializableExtra(KEY_GESTURE) as? Gesture
fun Intent.setGesture(gesture: Gesture) = putExtra(KEY_GESTURE, gesture)

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
fun Intent.getId() = getIntExtra(KEY_ID, -1)
fun Intent.setId(id: Int) = putExtra(KEY_ID, id)

/** Slug **/
private const val KEY_SLUG = "slug"
fun Intent.getSlug() = getStringExtra(KEY_SLUG)
fun Intent.setSlug(slug: String) = putExtra(KEY_SLUG, slug)

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


