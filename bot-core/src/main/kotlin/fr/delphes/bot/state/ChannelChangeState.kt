package fr.delphes.bot.state

import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.api.streams.Stream

interface ChannelChangeState {
    fun changeCurrentStream(newStream: Stream?)
    suspend fun addMessage(userMessage: UserMessage)
    suspend fun newFollow(newFollow: User)
    suspend fun newSub(newSub: User)
}