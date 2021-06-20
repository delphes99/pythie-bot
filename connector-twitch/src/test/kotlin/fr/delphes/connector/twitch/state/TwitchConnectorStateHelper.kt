package fr.delphes.connector.twitch.state

import fr.delphes.twitch.TwitchChannel
import io.mockk.every
import io.mockk.mockk

fun mockBotAccountProvider(channel: String): BotAccountProvider {
    return mockk { every { botAccount } returns TwitchChannel(channel) }
}