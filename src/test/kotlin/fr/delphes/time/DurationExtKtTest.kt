package fr.delphes.time

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Duration

internal class DurationExtKtTest {
    @Test
    internal fun `minute and second`() {
        assertThat(Duration.parse("PT9M11.4806089S").prettyPrint()).isEqualTo("9m11s")
    }

    @Test
    internal fun `zero`() {
        assertThat(Duration.ZERO.prettyPrint()).isEqualTo("0s")
    }
}