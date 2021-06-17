package nl.appt.tabs.home

import nl.appt.MainActivity
import nl.appt.R
import nl.appt.model.HomeAppLinkModel
import nl.appt.model.HomeLinkModel
import nl.appt.model.HomePagerModel
import nl.appt.model.HomeTrainingModel

object UserBlocksManager {

    val userBlocksData = arrayListOf(
        R.string.home_user_title,
        HomeTrainingModel(R.drawable.icon_tiles_training, R.string.home_block_title_training),
        HomeLinkModel(
            R.drawable.icon_tiles_meldpunt,
            R.string.home_block_title_meldpunt,
            R.string.home_block_link_meldpunt
        ),
        HomeLinkModel(
            R.drawable.icon_tiles_community,
            R.string.home_block_title_comminity,
            R.string.home_block_link_community
        ),
        HomeAppLinkModel(
            R.drawable.icon_tiles_overappt,
            R.string.home_block_title_overappt,
            R.string.home_block_link_overappt
        ),
    )

    val professionalBlocksData = arrayListOf(
        R.string.home_professional_title,
        HomePagerModel(
            R.drawable.icon_tiles_kennisbank,
            R.string.home_block_title_kennisbank,
            MainActivity.KNOWLEDGE_FRAGMENT_NUMBER
        ),
        HomeAppLinkModel(
            R.drawable.icon_tiles_aanpak,
            R.string.home_block_title_aanpak,
            R.string.home_block_link_aanpak
        ),
        HomeTrainingModel(R.drawable.icon_tiles_training, R.string.home_block_title_training),
        HomePagerModel(
            R.drawable.icon_tiles_diensten,
            R.string.home_block_title_deinsten,
            MainActivity.SERVICE_FRAGMENT_NUMBER
        ),
        HomeAppLinkModel(
            R.drawable.icon_tiles_overappt,
            R.string.home_block_title_overappt,
            R.string.home_block_link_overappt
        ),
    )
}