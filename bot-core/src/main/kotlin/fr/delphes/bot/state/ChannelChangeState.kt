package fr.delphes.bot.state

import fr.delphes.twitch.api.streams.Stream
import fr.delphes.twitch.api.user.UserName

interface ChannelChangeState {
    fun changeCurrentStream(newStream: Stream?)
    suspend fun addMessage(userMessage: UserMessage)
    suspend fun newFollow(newFollow: UserName)
    suspend fun newSub(newSub: UserName)
    suspend fun newCheer(cheerer: UserName?, bits: Long)
}