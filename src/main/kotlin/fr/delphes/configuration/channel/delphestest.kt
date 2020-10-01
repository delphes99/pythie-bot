package fr.delphes.configuration.channel

import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.event.outgoing.SendMessage
import fr.delphes.feature.command.Command
import fr.delphes.feature.newFollow.NewFollowFeature
import fr.delphes.feature.streamOffline.StreamOfflineFeature
import fr.delphes.feature.streamOnline.StreamOnlineFeature
import fr.delphes.feature.voth.FileVOTHStateRepository
import fr.delphes.feature.voth.VOTH
import fr.delphes.feature.voth.VOTHConfiguration
import fr.delphes.time.prettyPrint
import java.time.Duration
import java.util.*

/**
 * Example for delphestest channel : https://www.twitch.tv/delphestest
 */
val delphestestChannel = ChannelConfiguration.build("./configuration-delphestest.properties") { properties ->
    ChannelConfiguration(
        properties.getProperty("channel.name"),
        properties.getProperty("account.oAuth"),
        Command(
            "!test",
            cooldown = Duration.ofSeconds(10),
            reponses = listOf(
                SendMessage("Compte de test opérationnel !")
            )
        ),
        NewFollowFeature { newFollow ->
            listOf(SendMessage("Merci du follow ${newFollow.follower.name}"))
        },
        StreamOfflineFeature { listOf(SendMessage("Le stream est fini, au revoir !")) },
        StreamOnlineFeature { listOf(SendMessage("Le stream démarre, ravi de vous revoir !")) }
    )
}