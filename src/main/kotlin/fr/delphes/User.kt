package fr.delphes

data class User(val name: String) {
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