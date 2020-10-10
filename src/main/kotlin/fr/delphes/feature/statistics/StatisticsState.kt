package fr.delphes.feature.statistics

import fr.delphes.feature.State

class StatisticsState(
    private val userMessages: MutableList<UserMessage> = mutableListOf()
) : State {
    val numberMessages: Int get() = userMessages.count()
    val numberOfChatters: Int get() = userMessages.map(UserMessage::user).distinct().count()

    fun addMessage(userMessage: UserMessage) {
        userMessages.add(userMessage)
    }
}