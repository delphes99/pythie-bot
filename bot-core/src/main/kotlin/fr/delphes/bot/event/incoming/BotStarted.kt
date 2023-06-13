package fr.delphes.bot.event.incoming

import fr.delphes.annotation.incomingEvent.RegisterIncomingEvent
import kotlinx.serialization.Serializable

@Serializable
@RegisterIncomingEvent
object BotStarted : IncomingEvent
