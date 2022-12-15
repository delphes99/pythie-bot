package fr.delphes.features.twitch.newSub

import fr.delphes.connector.twitch.incomingEvent.NewSub
import fr.delphes.features.twitch.SimpleTwitchEventFeature
import fr.delphes.features.twitch.TwitchEventParameters
import fr.delphes.rework.feature.FeatureId
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.uuid.uuid

class CustomNewSub(
    override val channel: TwitchChannel,
    override val id: FeatureId = FeatureId(uuid()),
    action: suspend TwitchEventParameters<NewSub>.() -> Unit
) : SimpleTwitchEventFeature<NewSub>(NewSub::class, id, action)