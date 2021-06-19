package fr.delphes.connector.twitch.incomingEvent

data class Poll(
    val id: String,
    val title: String,
    val choices: List<PollChoice>,
)