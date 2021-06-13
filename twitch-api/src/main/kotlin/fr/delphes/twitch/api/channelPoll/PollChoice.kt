package fr.delphes.twitch.api.channelPoll

import kotlinx.serialization.Serializable

@Serializable
data class PollChoice(val title: String)
