package fr.delphes.connector.twitch.eventMapper

import fr.delphes.twitch.api.user.UserName
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe

class VIPParserTest : ShouldSpec({
    should("non vip list") {
        VIPParser.extractVips("toto") shouldBe VIPParser.VIPResult.NotVIPResult
    }

    should("vip list with one name") {
        val extractVips = VIPParser.extractVips("The VIPs of this channel are: glacios_.")

        (extractVips as VIPParser.VIPResult.VIPList).users.shouldContainExactly(UserName("glacios_"))
    }

    should("vip list with several names") {
        val extractVips = VIPParser.extractVips("The VIPs of this channel are: glacios_, crazymeal, ghostcatfr, jimmy_fr_60.")

        (extractVips as VIPParser.VIPResult.VIPList).users.shouldContainExactly(
            UserName("glacios_"),
            UserName("crazymeal"),
            UserName("ghostcatfr"),
            UserName("jimmy_fr_60")
        )
    }

    should("empty vip list") {
        val extractVips = VIPParser.extractVips("This channel does not have any VIPs.")

        (extractVips as VIPParser.VIPResult.VIPList).users.shouldBeEmpty()
    }
})