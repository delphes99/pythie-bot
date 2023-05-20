package fr.delphes.features.twitch.clipCreated

import fr.delphes.bot.event.eventHandler.EventHandlerContext
import fr.delphes.connector.twitch.incomingEvent.ClipCreated
import fr.delphes.features.twitch.SimpleTwitchEventFeature
import fr.delphes.rework.feature.FeatureId
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.uuid.uuid

class CustomClipCreated(
    override val channel: TwitchChannel,
    override val id: FeatureId = FeatureId(uuid()),
    action: EventHandlerContext<ClipCreated>,
) : SimpleTwitchEventFeature<ClipCreated>(ClipCreated::class, id, action)