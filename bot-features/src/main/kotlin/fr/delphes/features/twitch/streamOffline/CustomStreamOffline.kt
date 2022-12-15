package fr.delphes.features.twitch.streamOffline

import fr.delphes.connector.twitch.incomingEvent.StreamOffline
import fr.delphes.features.twitch.SimpleTwitchEventFeature
import fr.delphes.features.twitch.TwitchEventParameters
import fr.delphes.rework.feature.FeatureId
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.uuid.uuid

class CustomStreamOffline(
    override val channel: TwitchChannel,
    override val id: FeatureId = FeatureId(uuid()),
    action: suspend TwitchEventParameters<StreamOffline>.() -> Unit
) : SimpleTwitchEventFeature<StreamOffline>(StreamOffline::class, id, action)