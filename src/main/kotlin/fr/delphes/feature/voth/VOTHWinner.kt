package fr.delphes.feature.voth

import fr.delphes.User
import fr.delphes.event.incoming.RewardCost
import java.time.LocalDateTime

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