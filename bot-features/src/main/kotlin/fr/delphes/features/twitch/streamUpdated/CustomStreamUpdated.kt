package fr.delphes.features.twitch.streamUpdated

import fr.delphes.connector.twitch.incomingEvent.StreamChanged
import fr.delphes.features.twitch.SimpleTwitchEventFeature
import fr.delphes.features.twitch.TwitchEventParameters
import fr.delphes.rework.feature.FeatureId
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.uuid.uuid

class CustomStreamUpdated(
    override val channel: TwitchChannel,
    override val id: FeatureId = FeatureId(uuid()),
    action: suspend TwitchEventParameters<StreamChanged>.() -> Unit
) : SimpleTwitchEventFeature<StreamChanged>(StreamChanged::class, id, action)