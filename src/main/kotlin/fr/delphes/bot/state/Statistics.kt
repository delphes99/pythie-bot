package fr.delphes.bot.state

import fr.delphes.User

class Statistics(
    private val userMessages: MutableList<UserMessage> = mutableListOf(),
    private val newFollow: MutableList<User> = mutableListOf(),
    private val newSub: MutableList<User> = mutableListOf()
) : UpdateStatistics {
    val numberOfFollow: Int get() = newFollow.size
    val numberOfSub: Int get() = newSub.size
    val numberMessages: Int get() = userMessages.count()
    val numberOfChatters: Int get() = userMessages.map(UserMessage::user).distinct().count()

    val messages: List<UserMessage> get() = userMessages
    val lastFollows: List<User> get() = newFollow.takeLast(3).reversed()

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