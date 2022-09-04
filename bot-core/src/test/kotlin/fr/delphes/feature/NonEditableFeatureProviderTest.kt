package fr.delphes.feature

import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class NonEditableFeatureProviderTest {
    @Test
    internal fun `keep non editable features`() {
        runBlocking {
            val manager = NonEditableFeatureProvider(
                listOf(
                    PersonnaFeature.WORKING
                )
            )

            manager.get().shouldContainExactlyInAnyOrder(PersonnaFeature.WORKING)
        }
    }
}