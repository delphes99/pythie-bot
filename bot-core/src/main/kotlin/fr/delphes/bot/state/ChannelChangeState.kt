package fr.delphes.bot.state

import fr.delphes.User
import fr.delphes.twitch.model.Stream

interface ChannelChangeState {
    fun changeCurrentStream(newStream: Stream?)
    fun addMessage(userMessage: UserMessage)
    fun newFollow(newFollow: User)
    fun newSub(newSub: User)
}