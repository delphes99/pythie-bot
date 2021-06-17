package fr.delphes.bot.state

import fr.delphes.twitch.api.user.User

interface UpdateStatistics {
    fun addMessage(userMessage: UserMessage)
    fun newFollow(newFollow: User)
    fun newSub(newSub: User)
    fun newCheer(cheerer: User?, bits: Long)
}