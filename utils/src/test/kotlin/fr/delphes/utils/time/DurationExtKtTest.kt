package fr.delphes.utils.time

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import java.time.Duration

class DurationExtKtTest : ShouldSpec({
    should("minute and second") {
        Duration.parse("PT9M11.4806089S").prettyPrint() shouldBe "9m11s"
    }

    should("zero") {
        Duration.ZERO.prettyPrint() shouldBe "0s"
    }
})