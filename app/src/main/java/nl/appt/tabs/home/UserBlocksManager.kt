package nl.appt.tabs.home

import nl.appt.MainActivity
import nl.appt.R
import nl.appt.model.HomeLinkModel
import nl.appt.model.HomePagerModel
import nl.appt.model.HomeTrainingModel

object UserBlocksManager {

    //Titles
    private const val TRAINING_TITLE = "Training"
    private const val MELDPUNT_TITLE = "Meldpunt"
    private const val COMMUNITY_TITLE = "Community"
    private const val OVER_APPT_TITLE = "Over Appt"
    private const val KENNISBANK_TITLE = "Kennisbank"
    private const val AANPAK_TITLE = "Aanpak"
    private const val DEINSTEN_TITLE = "Diensten"

    //Links
    private const val MELDPUNT_LINK = "https://appt.nl/meldpunt"
    private const val COMMUNITY_LINK = "https://www.facebook.com/groups/1302246033296587"
    private const val OVERAPPT_LINK = "https://appt.nl/over"
    private const val AANPAK_LINK = "https://appt.nl/kennisbank/aanpak"

    fun getUserBlocks(): ArrayList<Any> {
        return userBlocksData
    }

    fun getProfessionalBlocks(): ArrayList<Any> {
        return professionalBlocksData
    }

    private val userBlocksData = arrayListOf(
        HomeTrainingModel(R.drawable.icon_tiles_training, TRAINING_TITLE),
        HomeLinkModel(R.drawable.icon_tiles_meldpunt, MELDPUNT_TITLE, MELDPUNT_LINK),
        HomeLinkModel(R.drawable.icon_tiles_community, COMMUNITY_TITLE, COMMUNITY_LINK),
        HomeLinkModel(R.drawable.icon_tiles_overappt, OVER_APPT_TITLE, OVERAPPT_LINK),
    )

    private val professionalBlocksData = arrayListOf(
        HomePagerModel(
            R.drawable.icon_tiles_kennisbank,
            KENNISBANK_TITLE,
            MainActivity.KNOWLEDGE_FRAGMENT_NUMBER
        ),
        HomeLinkModel(R.drawable.icon_tiles_aanpak, AANPAK_TITLE, AANPAK_LINK),
        HomeTrainingModel(R.drawable.icon_tiles_training, TRAINING_TITLE),
        HomePagerModel(
            R.drawable.icon_tiles_diensten,
            DEINSTEN_TITLE,
            MainActivity.SERVICE_FRAGMENT_NUMBER
        ),
        HomeLinkModel(R.drawable.icon_tiles_overappt, OVER_APPT_TITLE, OVERAPPT_LINK),
    )
}