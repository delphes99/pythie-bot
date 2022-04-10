@file:UseSerializers(DurationSerializer::class, NullableLocalDateTimeSerializer::class)

package fr.delphes.features.twitch.command

import fr.delphes.bot.event.outgoing.OutgoingEventBuilder
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.feature.ChannelFilter
import fr.delphes.connector.twitch.feature.TwitchFeatureConfiguration
import fr.delphes.connector.twitch.feature.WithCommand
import fr.delphes.feature.featureNew.FeatureDescription
import fr.delphes.feature.featureNew.FeatureDescriptionItem
import fr.delphes.feature.featureNew.FeatureDescriptionItemType.OUTGOING_EVENTS
import fr.delphes.feature.featureNew.FeatureDescriptionItemType.STRING
import fr.delphes.feature.featureNew.FeatureIdentifier
import fr.delphes.feature.featureNew.FeatureRuntime
import fr.delphes.feature.featureNew.FeatureState
import fr.delphes.feature.featureNew.IncomingEventFilters
import fr.delphes.feature.featureNew.RuntimeResult
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
import kotlinx.serialization.json.Json
import java.time.Duration
import java.time.LocalDateTime
private const val type = "twitch-incoming-command"

//TODO delete old command
@Serializable
@SerialName(type)
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
                TriggerFilter(command),
                CooldownFilter(cooldown, clock)
            ),
            NewTwitchCommandState()
        ) { _, _ ->
            RuntimeResult.NewState(
                NewTwitchCommandState(clock.now()),
                response.map(OutgoingEventBuilder::build)
            )
        }
    }

    override val commands: Set<Command> get() = setOf(command)

    override fun description(serializer: Json): FeatureDescription {
        return FeatureDescription(
            identifier,
            type,
            listOf(
                FeatureDescriptionItem.build(STRING, "channel", this.channel, serializer),
                FeatureDescriptionItem.build(STRING, "trigger", this.trigger, serializer),
                FeatureDescriptionItem.build(OUTGOING_EVENTS, "response", this.response, serializer),
            )
        )
    }
}

@Serializable
class NewTwitchCommandState(
    val lastCall: LocalDateTime? = null
) : FeatureState