package fr.delphes.connector.twitch.user

import fr.delphes.twitch.api.user.UserId
import fr.delphes.twitch.api.user.payload.BroadcasterType
import java.time.LocalDateTime

data class UserInfos(
    val name: String,
    val id: UserId,
    val broadcasterType: BroadcasterType,
    val viewCount: Long,
    val lastStreamTitle: String? = null,
    val lastStreamDate: LocalDateTime? = null,
) {
    fun isStreamer(): Boolean {
        return broadcasterType == BroadcasterType.AFFILIATE || broadcasterType == BroadcasterType.PARTNER || lastStreamDate != null
    }

    fun hasStreamedSince(date: LocalDateTime): Boolean {
        return lastStreamDate?.isAfter(date) ?: false
    }
}
