package fr.delphes.connector.twitch.eventMapper

import fr.delphes.twitch.api.user.User
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class VIPParserTest {
    @Test
    fun `non vip list`() {
        VIPParser.extractVips("toto") shouldBe VIPParser.VIPResult.NotVIPResult
    }

    @Test
    fun `vip list with one name`() {
        val extractVips = VIPParser.extractVips("The VIPs of this channel are: glacios_.")

        (extractVips as VIPParser.VIPResult.VIPList).users.shouldContainExactly(User("glacios_"))
    }

    @Test
    fun `vip list with several names`() {
        val extractVips = VIPParser.extractVips("The VIPs of this channel are: glacios_, crazymeal, ghostcatfr, jimmy_fr_60.")

        (extractVips as VIPParser.VIPResult.VIPList).users.shouldContainExactly(
            User("glacios_"),
            User("crazymeal"),
            User("ghostcatfr"),
            User("jimmy_fr_60")
        )
    }

    @Test
    fun `empty vip list`() {
        val extractVips = VIPParser.extractVips("This channel does not have any VIPs.")

        (extractVips as VIPParser.VIPResult.VIPList).users.shouldBeEmpty()
    }
}