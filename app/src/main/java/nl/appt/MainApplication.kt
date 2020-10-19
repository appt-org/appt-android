package nl.appt

import android.app.Application
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

/**
 * Created by Jan Jaap de Groot on 19/10/2020
 * Copyright 2020 Stichting Appt
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Calligraphy
        ViewPump.init(ViewPump.builder()
            .addInterceptor(CalligraphyInterceptor(
                CalligraphyConfig.Builder()
                    .setDefaultFontPath(getString(R.string.font_regular))
                    .setFontAttrId(R.attr.fontPath)
                    .build()))
            .build())
    }
}