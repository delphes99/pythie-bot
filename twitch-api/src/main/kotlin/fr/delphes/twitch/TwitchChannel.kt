package fr.delphes.twitch

data class TwitchChannel(
    val name: String
) {
    val normalizeName = name.toLowerCase()

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