package fr.delphes.connector.twitch.state

import fr.delphes.bot.connector.ConnectorState
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.user.UserInfos
import fr.delphes.state.StateId
import fr.delphes.twitch.api.user.UserName

class GetUserInfos(private val connector: TwitchConnector) : ConnectorState {
    override val id = ID

    suspend fun getUserInfos(user: UserName): UserInfos? {
        return connector.getUser(user)
    }

    companion object {
        val ID = StateId.from<GetUserInfos>("twitch-connector-get-user-infos")
    }
}