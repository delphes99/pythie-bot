package fr.delphes.bot.state

import fr.delphes.twitch.api.channelCheer.NewCheer
import fr.delphes.twitch.api.user.User
import kotlinx.serialization.Serializable

@Serializable
class Statistics(
    private val userMessages: MutableList<UserMessage> = mutableListOf(),
    private val newFollows: MutableList<User> = mutableListOf(),
    private val newSubs: MutableList<User> = mutableListOf(),
    private val cheers: MutableList<UserCheer> = mutableListOf()
) : UpdateStatistics {
    val numberOfFollow: Int get() = newFollows.size
    val numberOfSub: Int get() = newSubs.size
    val numberMessages: Int get() = userMessages.count()
    val numberOfChatters: Int get() = userMessages.map(UserMessage::user).distinct().count()

    val lastFollows: List<User> get() = newFollows
    val lastSubs: List<User> get() = newSubs
    val lastCheers: List<UserCheer> get() = cheers

    override fun addMessage(userMessage: UserMessage) {
        userMessages.add(userMessage)
    }

    override fun newFollow(newFollow: User) {
        if(!this.newFollows.contains(newFollow)) {
            this.newFollows.add(0, newFollow)
        }
    }

    override fun newSub(newSub: User) {
        if(!this.newSubs.contains(newSub)) {
            this.newSubs.add(0, newSub)
        }
    }

    override fun newCheer(newCheer: NewCheer) {
        this.cheers.add(0, UserCheer(newCheer.cheerer, newCheer.bits))
    }
}