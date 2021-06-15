package nl.appt.tabs.home

import androidx.core.net.toUri
import nl.appt.MainActivity
import nl.appt.R
import nl.appt.model.HomeAppLinkModel
import nl.appt.model.HomeLinkModel
import nl.appt.model.HomePagerModel
import nl.appt.model.HomeTrainingModel

object UserBlocksManager {

    //Titles
    private const val COMMUNITY_TITLE = "Community"
    private const val TRAINING_TITLE = "Training"
    private const val MELDPUNT_TITLE = "Meldpunt"
    private const val OVER_APPT_TITLE = "Over Appt"
    private const val KENNISBANK_TITLE = "Kennisbank"
    private const val AANPAK_TITLE = "Aanpak"
    private const val DEINSTEN_TITLE = "Diensten"

    //Links
    private const val MELDPUNT_LINK = "https://appt.crio-server.com/meldpunt"
    private const val COMMUNITY_LINK = "https://www.facebook.com/groups/1302246033296587"
    private const val OVERAPPT_LINK = "https://appt.crio-server.com/over"
    private const val AANPAK_LINK = "https://appt.crio-server.com/kennisbank/aanpak"

    val userBlocksData = arrayListOf(
        R.string.home_user_description,
        HomeTrainingModel(R.drawable.icon_tiles_training, TRAINING_TITLE),
        HomeAppLinkModel(R.drawable.icon_tiles_meldpunt, MELDPUNT_TITLE, MELDPUNT_LINK.toUri()),
        HomeLinkModel(R.drawable.icon_tiles_community, COMMUNITY_TITLE, COMMUNITY_LINK),
        HomeAppLinkModel(R.drawable.icon_tiles_overappt, OVER_APPT_TITLE, OVERAPPT_LINK.toUri()),
    )

    val professionalBlocksData = arrayListOf(
        R.string.home_professional_description,
        HomePagerModel(
            R.drawable.icon_tiles_kennisbank,
            KENNISBANK_TITLE,
            MainActivity.KNOWLEDGE_FRAGMENT_NUMBER
        ),
        HomeAppLinkModel(R.drawable.icon_tiles_aanpak, AANPAK_TITLE, AANPAK_LINK.toUri()),
        HomeTrainingModel(R.drawable.icon_tiles_training, TRAINING_TITLE),
        HomePagerModel(
            R.drawable.icon_tiles_diensten,
            DEINSTEN_TITLE,
            MainActivity.SERVICE_FRAGMENT_NUMBER
        ),
        HomeAppLinkModel(R.drawable.icon_tiles_overappt, OVER_APPT_TITLE, OVERAPPT_LINK.toUri()),
    )
}