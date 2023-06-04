package fr.delphes.bot.event.incoming

import fr.delphes.utils.uuid.uuid
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class IncomingEventId(val id: String = uuid())