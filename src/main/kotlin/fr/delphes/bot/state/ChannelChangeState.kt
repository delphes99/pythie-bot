package fr.delphes.bot.state

interface ChannelChangeState {
    fun changeCurrentStream(newStream: CurrentStream?)
}