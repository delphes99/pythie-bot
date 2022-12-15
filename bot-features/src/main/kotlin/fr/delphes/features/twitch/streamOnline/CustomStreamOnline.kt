package fr.delphes.features.twitch.streamOnline

import fr.delphes.connector.twitch.incomingEvent.StreamOnline
import fr.delphes.features.twitch.SimpleTwitchEventFeature
import fr.delphes.features.twitch.TwitchEventParameters
import fr.delphes.rework.feature.FeatureId
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.uuid.uuid

class CustomStreamOnline(
    override val channel: TwitchChannel,
    override val id: FeatureId = FeatureId(uuid()),
    action: suspend TwitchEventParameters<StreamOnline>.() -> Unit
) : SimpleTwitchEventFeature<StreamOnline>(StreamOnline::class, id, action)