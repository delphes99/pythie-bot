package fr.delphes.feature

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import kotlinx.coroutines.test.runTest

class NonEditableFeatureProviderTest : ShouldSpec({
    should("keep non editable features") {
        runTest {
            val manager = NonEditableFeatureProvider(
                listOf(
                    PersonaFeature.WORKING
                )
            )

            manager.get().shouldContainExactlyInAnyOrder(PersonaFeature.WORKING)
        }
    }
})