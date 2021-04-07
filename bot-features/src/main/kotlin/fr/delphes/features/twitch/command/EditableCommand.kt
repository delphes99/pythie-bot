package fr.delphes.features.twitch.command

import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.command.SimpleCommandHandler
import fr.delphes.feature.EditableFeature
import fr.delphes.twitch.TwitchChannel
import java.time.Duration

class EditableCommand(
    val configuration: EditableCommandConfiguration,
) : EditableFeature<EditableCommandConfiguration>, TwitchFeature {
    override val channel: TwitchChannel = TwitchChannel(configuration.channel)

    override fun description() = configuration

    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(commandHandler)
    }
    private val commandHandler = SimpleCommandHandler(
        channel,
        Command(configuration.trigger),
        cooldown = Duration.ofSeconds(configuration.cooldown),
        responses = {
            configuration.responses.map { it.build() }
        }
    )

    override val commands: Iterable<Command> = listOf(commandHandler.command)
}