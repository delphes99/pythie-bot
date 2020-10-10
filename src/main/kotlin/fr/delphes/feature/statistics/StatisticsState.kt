package fr.delphes.feature.statistics

import fr.delphes.User
import fr.delphes.feature.State

class StatisticsState(
    private val userMessages: MutableList<UserMessage> = mutableListOf()
) : State {
    val numberOfFollow: Int get() = newFollow.size
    val numberOfSub: Int get() = newSub.size
    val numberMessages: Int get() = userMessages.count()
    val numberOfChatters: Int get() = userMessages.map(UserMessage::user).distinct().count()

    private val newFollow = mutableSetOf<User>()
    private val newSub = mutableSetOf<User>()

    fun addMessage(userMessage: UserMessage) {
        userMessages.add(userMessage)
    }

    fun newFollow(newFollow: User) {
        this.newFollow.add(newFollow)
    }

    fun newSub(newSub: User) {
        this.newSub.add(newSub)
    }
}