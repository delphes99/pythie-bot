package fr.delphes.features.twitch

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.feature.ChannelFilter
import fr.delphes.connector.twitch.feature.TwitchFeatureConfiguration
import fr.delphes.connector.twitch.feature.WithCommand
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.connector.twitch.outgoingEvent.SendMessage
import fr.delphes.feature.featureNew.FeatureIdentifier
import fr.delphes.feature.featureNew.FeatureRuntime
import fr.delphes.feature.featureNew.IncomingEventFilter
import fr.delphes.feature.featureNew.IncomingEventFilters
import fr.delphes.twitch.TwitchChannel
import kotlinx.serialization.Serializable

//TODO delete old command
@Serializable
class NewTwitchCommand(
    override val identifier: FeatureIdentifier,
    override val channel: TwitchChannel,
    private val trigger: String,
    private val response: String
) : TwitchFeatureConfiguration, WithCommand {
    val command: Command get() = Command(trigger)

    override fun buildRuntime(): FeatureRuntime {
        return FeatureRuntime(
            IncomingEventFilters(
                ChannelFilter(channel),
                TriggerFilter()
            )
        ) {
            listOf(
                SendMessage(response, channel)
            )
        }
    }

    inner class TriggerFilter : IncomingEventFilter {
        override fun isApplicable(event: IncomingEvent): Boolean {
            return event is CommandAsked && event.command == command
        }
    }

    override val commands: Set<Command> = setOf(command)
}

