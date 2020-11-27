package fr.delphes.twitch.api.reward

import fr.delphes.twitch.api.user.User

data class RewardRedemption(
    val reward: Reward,
    val user: User,
    val cost: RewardCost
)