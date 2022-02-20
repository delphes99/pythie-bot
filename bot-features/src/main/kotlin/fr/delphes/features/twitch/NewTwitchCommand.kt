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
import fr.delphes.feature.featureNew.IncomingEventFilter
import fr.delphes.feature.featureNew.IncomingEventFilters
import fr.delphes.twitch.TwitchChannel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//TODO delete old command
@Serializable
@SerialName("twitch-incoming-command")
class NewTwitchCommand(
    override val identifier: FeatureIdentifier,
    override val channel: TwitchChannel,
    private val trigger: String,
    private val response: List<OutgoingEventBuilder>
) : TwitchFeatureConfiguration, WithCommand {
    val command: Command get() = Command(trigger)

    override fun buildRuntime(): FeatureRuntime {
        return FeatureRuntime(
            IncomingEventFilters(
                ChannelFilter(channel),
                TriggerFilter()
            )
        ) {
            response.map(OutgoingEventBuilder::build)
        }
    }

    inner class TriggerFilter : IncomingEventFilter {
        override fun isApplicable(event: IncomingEvent): Boolean {
            return event is CommandAsked && event.command == command
        }
    }

    override val commands: Set<Command> = setOf(command)
}

