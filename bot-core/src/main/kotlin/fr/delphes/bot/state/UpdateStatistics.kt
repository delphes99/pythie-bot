package fr.delphes.bot.state

import fr.delphes.twitch.model.User

interface UpdateStatistics {
    fun addMessage(userMessage: UserMessage)
    fun newFollow(newFollow: User)
    fun newSub(newSub: User)
}