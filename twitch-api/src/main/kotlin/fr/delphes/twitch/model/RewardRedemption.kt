package fr.delphes.twitch.model

data class RewardRedemption(
    val reward: Reward,
    val user: User,
    val cost: RewardCost
)