package fr.delphes.bot.state

import fr.delphes.twitch.api.user.User
import kotlinx.serialization.Serializable

@Serializable
class StreamStatistics(
    val streamId: String,
    private val userMessages: MutableList<UserMessage> = mutableListOf(),
    private val newFollows: MutableList<User> = mutableListOf(),
    private val newSubs: MutableList<User> = mutableListOf()
): UpdateStatistics {
    val messages: List<UserMessage> get() = userMessages

    val numberOfFollow: Int get() = newFollows.size
    val numberOfSub: Int get() = newSubs.size
    val numberMessages: Int get() = userMessages.count()
    val numberOfChatters: Int get() = userMessages.map(UserMessage::user).distinct().count()

    override fun addMessage(userMessage: UserMessage) {
        userMessages.add(userMessage)
    }

    override fun newFollow(newFollow: User) {
        if(!this.newFollows.contains(newFollow)) {
            this.newFollows.add(newFollow)
        }
    }

    override fun newSub(newSub: User) {
        if(!this.newSubs.contains(newSub)) {
            this.newSubs.add(newSub)
        }
    }
}