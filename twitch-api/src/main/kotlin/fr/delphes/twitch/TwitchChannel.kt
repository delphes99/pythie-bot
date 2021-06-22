package fr.delphes.twitch

import fr.delphes.twitch.api.user.User
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = TwitchChannelSerializer::class)
data class TwitchChannel(
    val name: String
) {
    val normalizeName = name.lowercase()

    fun toUser(): User {
        return User(name)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TwitchChannel

        if (normalizeName != other.normalizeName) return false

        return true
    }

    override fun hashCode(): Int {
        return normalizeName.hashCode()
    }
}

class TwitchChannelSerializer : KSerializer<TwitchChannel> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("TwitchChannel", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: TwitchChannel) = encoder.encodeString(value.name)

    override fun deserialize(decoder: Decoder): TwitchChannel = TwitchChannel(decoder.decodeString())
}
