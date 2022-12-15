package fr.delphes.features.twitch.newFollow

import fr.delphes.connector.twitch.incomingEvent.NewFollow
import fr.delphes.features.twitch.SimpleTwitchEventFeature
import fr.delphes.features.twitch.TwitchEventParameters
import fr.delphes.rework.feature.FeatureId
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.uuid.uuid

class CustomNewFollow(
    override val channel: TwitchChannel,
    override val id: FeatureId = FeatureId(uuid()),
    action: suspend TwitchEventParameters<NewFollow>.() -> Unit
) : SimpleTwitchEventFeature<NewFollow>(NewFollow::class, id, action)
