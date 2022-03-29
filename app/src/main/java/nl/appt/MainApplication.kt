package nl.appt

import android.app.Application
import com.github.kittinunf.fuel.core.FuelManager
import com.google.firebase.FirebaseApp
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

        // Firebase
        FirebaseApp.initializeApp(this)

        // Fuel
        FuelManager.instance.basePath = BuildConfig.BASE_URL
        FuelManager.instance.baseHeaders = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/61.0",
            "Platform" to "Android",
            "Version" to BuildConfig.VERSION_NAME
        )

        // Calligraphy
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath(getString(R.string.font_regular))
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )
    }
}