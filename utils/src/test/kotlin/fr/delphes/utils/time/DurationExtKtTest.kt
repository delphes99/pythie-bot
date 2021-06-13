package fr.delphes.utils.time

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.Duration

internal class DurationExtKtTest {
    @Test
    internal fun `minute and second`() {
        Duration.parse("PT9M11.4806089S").prettyPrint() shouldBe "9m11s"
    }

    @Test
    internal fun zero() {
        Duration.ZERO.prettyPrint() shouldBe "0s"
    }
}