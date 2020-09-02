package fr.delphes

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class VIPParserTest {
    @Test
    fun `non vip list`() {
        assertThat(VIPParser.extractVips("toto"))
            .isEqualTo(VIPParser.VIPResult.NotVIPResult)
    }

    @Test
    fun `vip list with one name`() {
        val extractVips = VIPParser.extractVips("The VIPs of this channel are: glacios_.")

        assertThat((extractVips as VIPParser.VIPResult.VIPList).users).containsExactlyInAnyOrder("glacios_")
    }

    @Test
    fun `vip list with several names`() {
        val extractVips = VIPParser.extractVips("The VIPs of this channel are: glacios_, crazymeal, ghostcatfr, jimmy_fr_60.")

        assertThat((extractVips as VIPParser.VIPResult.VIPList).users).containsExactlyInAnyOrder("glacios_", "crazymeal", "ghostcatfr", "jimmy_fr_60")
    }

    @Test
    fun `empty vip list`() {
        val extractVips = VIPParser.extractVips("This channel does not have any VIPs.")

        assertThat((extractVips as VIPParser.VIPResult.VIPList).users).isEmpty()
    }
}