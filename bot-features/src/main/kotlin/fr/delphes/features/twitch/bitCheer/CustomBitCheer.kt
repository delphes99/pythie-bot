package fr.delphes.features.twitch.bitCheer

import fr.delphes.connector.twitch.incomingEvent.BitCheered
import fr.delphes.features.twitch.SimpleTwitchEventFeature
import fr.delphes.features.twitch.TwitchEventParameters
import fr.delphes.rework.feature.FeatureId
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.uuid.uuid

class CustomBitCheer(
    override val channel: TwitchChannel,
    override val id: FeatureId = FeatureId(uuid()),
    action: suspend TwitchEventParameters<BitCheered>.() -> Unit
) : SimpleTwitchEventFeature<BitCheered>(BitCheered::class, id, action)