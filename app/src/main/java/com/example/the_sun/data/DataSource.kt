package com.example.the_sun.data

import com.example.the_sun.R
import com.example.the_sun.model.Solar_Image

object DataSource {

    val solarImages = listOf(
        Solar_Image(R.drawable.erupcion_solar, R.string.erupcion_solar),
        Solar_Image(R.drawable.corona_solar, R.string.corona_solar),
        Solar_Image(R.drawable.espiculas, R.string.espiculas),
        Solar_Image(R.drawable.filamentos, R.string.filamentos),
        Solar_Image(R.drawable.magnetosfera, R.string.magnetosfera),
        Solar_Image(R.drawable.mancha_solar, R.string.mancha_solar)
    )
}