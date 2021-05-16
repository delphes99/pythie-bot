package fr.delphes.twitch

import fr.delphes.twitch.api.user.User

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