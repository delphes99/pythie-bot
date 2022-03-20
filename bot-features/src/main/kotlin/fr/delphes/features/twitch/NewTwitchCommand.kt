@file:UseSerializers(DurationSerializer::class, NullableLocalDateTimeSerializer::class)

package fr.delphes.features.twitch

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEventBuilder
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.feature.ChannelFilter
import fr.delphes.connector.twitch.feature.TwitchFeatureConfiguration
import fr.delphes.connector.twitch.feature.WithCommand
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.feature.featureNew.FeatureIdentifier
import fr.delphes.feature.featureNew.FeatureRuntime
import fr.delphes.feature.featureNew.FeatureState
import fr.delphes.feature.featureNew.IncomingEventFilter
import fr.delphes.feature.featureNew.IncomingEventFilters
import fr.delphes.feature.featureNew.SimpleFeatureRuntime
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.serialization.DurationSerializer
import fr.delphes.utils.serialization.NullableLocalDateTimeSerializer
import fr.delphes.utils.time.Clock
import fr.delphes.utils.time.SystemClock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import java.time.Duration
import java.time.LocalDateTime

//TODO delete old command
@Serializable
@SerialName("twitch-incoming-command")
class NewTwitchCommand(
    override val identifier: FeatureIdentifier,
    override val channel: TwitchChannel,
    private val trigger: String,
    private val response: List<OutgoingEventBuilder>,
    private val cooldown: Duration? = null,
    @Transient
    private val clock: Clock = SystemClock
) : TwitchFeatureConfiguration<NewTwitchCommandState>, WithCommand {
    val command: Command get() = Command(trigger)

    override fun buildRuntime(): FeatureRuntime<NewTwitchCommandState> {
        return SimpleFeatureRuntime(
            IncomingEventFilters(
                ChannelFilter(channel),
                TriggerFilter(),
                CooldownFilter()
            ),
            NewTwitchCommandState()
        ) { _, _ -> NewTwitchCommandState(clock.now()) to response.map(OutgoingEventBuilder::build)
        }
    }

    inner class TriggerFilter : IncomingEventFilter<NewTwitchCommandState> {
        override fun isApplicable(event: IncomingEvent, state: NewTwitchCommandState): Boolean {
            return event is CommandAsked && event.command == command
        }
    }

    inner class CooldownFilter : IncomingEventFilter<NewTwitchCommandState> {
        override fun isApplicable(event: IncomingEvent, state: NewTwitchCommandState): Boolean {
            return cooldown?.let { state.lastCall?.plus(it) }?.isBefore(clock.now()) ?: true
        }
    }

    override val commands: Set<Command> get() = setOf(command)
}

@Serializable
class NewTwitchCommandState(
    val lastCall: LocalDateTime? = null
) : FeatureState