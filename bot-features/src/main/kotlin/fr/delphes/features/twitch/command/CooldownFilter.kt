package fr.delphes.features.twitch.command

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.feature.featureNew.IncomingEventFilter
import fr.delphes.utils.time.Clock
import java.time.Duration

class CooldownFilter(
    private val cooldown: Duration?,
    private val clock: Clock
) : IncomingEventFilter<NewTwitchCommandState> {
    override fun isApplicable(event: IncomingEvent, state: NewTwitchCommandState): Boolean {
        return cooldown?.let { state.lastCall?.plus(it) }?.isBefore(clock.now()) ?: true
    }
}