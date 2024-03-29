package fr.delphes.bot.state

import fr.delphes.twitch.api.user.UserName

@Deprecated("twitch specific statistics")
interface UpdateStatistics {
    fun addMessage(userMessage: UserMessage)
    fun newFollow(newFollow: UserName)
    fun newSub(newSub: UserName)
    fun newCheer(cheerer: UserName?, bits: Long)
}