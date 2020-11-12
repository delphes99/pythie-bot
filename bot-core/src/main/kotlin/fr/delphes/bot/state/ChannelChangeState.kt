package fr.delphes.bot.state

import fr.delphes.User

interface ChannelChangeState {
    fun changeCurrentStream(newStream: CurrentStream?)
    fun addMessage(userMessage: UserMessage)
    fun newFollow(newFollow: User)
    fun newSub(newSub: User)
}