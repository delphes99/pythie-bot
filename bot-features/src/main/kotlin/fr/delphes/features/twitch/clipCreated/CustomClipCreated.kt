package fr.delphes.features.twitch.clipCreated

import fr.delphes.connector.twitch.incomingEvent.ClipCreated
import fr.delphes.features.twitch.SimpleTwitchEventFeature
import fr.delphes.features.twitch.TwitchEventParameters
import fr.delphes.rework.feature.FeatureId
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.uuid.uuid

class CustomClipCreated(
    override val channel: TwitchChannel,
    override val id: FeatureId = FeatureId(uuid()),
    action: suspend TwitchEventParameters<ClipCreated>.() -> Unit
) : SimpleTwitchEventFeature<ClipCreated>(ClipCreated::class, id, action)