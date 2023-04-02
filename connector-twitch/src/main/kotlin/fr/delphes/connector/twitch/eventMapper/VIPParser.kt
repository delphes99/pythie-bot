package fr.delphes.connector.twitch.eventMapper

import fr.delphes.twitch.api.user.UserName
import java.util.regex.Pattern

object VIPParser {
    sealed class VIPResult {
        data class VIPList(val users: List<UserName>) : VIPResult()
        object NotVIPResult : VIPResult()
    }

    fun extractVips(privateMessage: String): VIPResult {
        val matcher = Pattern.compile("The VIPs of this channel are: (.*)\\.").matcher(privateMessage)
        if (matcher.matches()) {
            return VIPResult.VIPList(
                matcher.group(1)
                    .split(",")
                    .map(String::trim)
                    .map(::UserName)
            )
        }

        val matcherEmptyVIP = Pattern.compile("This channel does not have any VIPs\\.").matcher(privateMessage)
        if (matcherEmptyVIP.matches()) {
            return VIPResult.VIPList(emptyList())
        }

        return VIPResult.NotVIPResult
    }
}