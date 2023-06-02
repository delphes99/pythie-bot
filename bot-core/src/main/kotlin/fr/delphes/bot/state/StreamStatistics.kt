package fr.delphes.bot.state

import fr.delphes.twitch.api.user.UserName
import kotlinx.serialization.Serializable

@Serializable
@Deprecated("twitch specific statistics")
class StreamStatistics(
    val streamId: String,
    private val userMessages: MutableList<UserMessage> = mutableListOf(),
    private val newFollows: MutableList<UserName> = mutableListOf(),
    private val newSubs: MutableList<UserName> = mutableListOf(),
    private val cheers: MutableList<UserCheer> = mutableListOf(),
) : UpdateStatistics {
    val messages: List<UserMessage> get() = userMessages

    val numberOfFollow: Int get() = newFollows.size
    val numberOfSub: Int get() = newSubs.size
    val numberMessages: Int get() = userMessages.count()
    val numberOfChatters: Int get() = userMessages.map(UserMessage::user).distinct().count()

    override fun addMessage(userMessage: UserMessage) {
        userMessages.add(userMessage)
    }

    override fun newFollow(newFollow: UserName) {
        if (!this.newFollows.contains(newFollow)) {
            this.newFollows.add(0, newFollow)
        }
    }

    override fun newSub(newSub: UserName) {
        if (!this.newSubs.contains(newSub)) {
            this.newSubs.add(0, newSub)
        }
    }

    override fun newCheer(cheerer: UserName?, bits: Long) {
        this.cheers.add(0, UserCheer(cheerer, bits))
    }
}