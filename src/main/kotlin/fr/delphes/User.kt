package fr.delphes

import fr.delphes.bot.webserver.payload.NewFollowPayloadData
import kotlinx.serialization.Serializable

@Serializable
data class User(val name: String) {
    constructor(payload: NewFollowPayloadData) : this(
        payload.from_name
    )

    val normalizeName = name.toLowerCase()

    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (normalizeName != other.normalizeName) return false

        return true
    }

    override fun hashCode(): Int {
        return normalizeName.hashCode()
    }
}