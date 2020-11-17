package fr.delphes.twitch

interface PubSubApi {
    suspend fun listenToChannelReward()
}