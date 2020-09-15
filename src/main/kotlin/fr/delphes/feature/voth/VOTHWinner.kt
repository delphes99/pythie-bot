@file:UseSerializers(LocalDateTimeSerializer::class)

package fr.delphes.feature.voth

import fr.delphes.User
import fr.delphes.event.incoming.RewardCost
import fr.delphes.storage.serialization.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime

@Serializable
data class VOTHWinner(
    val user: User,
    val since: LocalDateTime,
    val cost: RewardCost
) {
    constructor(
        user: String,
        since: LocalDateTime,
        cost: RewardCost
    ) : this(User(user), since, cost)
}