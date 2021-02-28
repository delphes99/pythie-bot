package fr.delphes.connector.twitch.user

import fr.delphes.twitch.api.user.payload.BroadcasterType
import java.time.LocalDateTime

data class UserInfos(
    val name: String,
    val since: LocalDateTime,
    val broadcasterType: BroadcasterType,
    val viewCount: Long,
) {
    fun isStreamer(): Boolean {
        return broadcasterType == BroadcasterType.AFFILIATE || broadcasterType == BroadcasterType.PARTNER
    }
}
