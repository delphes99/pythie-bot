package fr.delphes.connector.twitch.eventMapper

import fr.delphes.twitch.api.user.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class VIPParserTest {
    @Test
    fun `non vip list`() {
        assertThat(VIPParser.extractVips("toto"))
            .isEqualTo(VIPParser.VIPResult.NotVIPResult)
    }

    @Test
    fun `vip list with one name`() {
        val extractVips = VIPParser.extractVips("The VIPs of this channel are: glacios_.")

        assertThat((extractVips as VIPParser.VIPResult.VIPList).users).containsExactlyInAnyOrder(User("glacios_"))
    }

    @Test
    fun `vip list with several names`() {
        val extractVips = VIPParser.extractVips("The VIPs of this channel are: glacios_, crazymeal, ghostcatfr, jimmy_fr_60.")

        assertThat((extractVips as VIPParser.VIPResult.VIPList).users).containsExactlyInAnyOrder(
            User("glacios_"),
            User("crazymeal"),
            User("ghostcatfr"),
            User("jimmy_fr_60")
        )
    }

    @Test
    fun `empty vip list`() {
        val extractVips = VIPParser.extractVips("This channel does not have any VIPs.")

        assertThat((extractVips as VIPParser.VIPResult.VIPList).users).isEmpty()
    }
}