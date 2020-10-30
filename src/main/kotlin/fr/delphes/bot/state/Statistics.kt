package fr.delphes.bot.state

import fr.delphes.User

class Statistics(
    private val userMessages: MutableList<UserMessage> = mutableListOf(),
    private val newFollow: MutableSet<User> = mutableSetOf(),
    private val newSub: MutableSet<User> = mutableSetOf()
) : UpdateStatistics {
    val numberOfFollow: Int get() = newFollow.size
    val numberOfSub: Int get() = newSub.size
    val numberMessages: Int get() = userMessages.count()
    val numberOfChatters: Int get() = userMessages.map(UserMessage::user).distinct().count()

    override fun addMessage(userMessage: UserMessage) {
        userMessages.add(userMessage)
    }

    override fun newFollow(newFollow: User) {
        this.newFollow.add(newFollow)
    }

    override fun newSub(newSub: User) {
        this.newSub.add(newSub)
    }
}