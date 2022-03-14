package nl.appt.extensions

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import nl.appt.R

fun ImageView.load(url: String) {
    val imageLoader = ImageLoader.Builder(this.context)
        .componentRegistry { add(SvgDecoder(this@load.context)) }
        .build()

    val color = ContextCompat.getColor(context, R.color.primary)

    val circularProgressDrawable = CircularProgressDrawable(this.context)
    circularProgressDrawable.setTint(color)
    circularProgressDrawable.setColorSchemeColors(color)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()

    val request = ImageRequest.Builder(this.context)
        .crossfade(true)
        .crossfade(250)
        .placeholder(circularProgressDrawable)
        .error(R.drawable.icon_placeholder)
        .data(url)
        .target(this)
        .build()

    imageLoader.enqueue(request)
}