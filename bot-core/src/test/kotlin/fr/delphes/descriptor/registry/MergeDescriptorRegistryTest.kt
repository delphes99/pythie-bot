package fr.delphes.descriptor.registry

import fr.delphes.feature.DescriptorTestUtil
import fr.delphes.feature.DescriptorTestUtil.descriptor1
import fr.delphes.feature.DescriptorTestUtil.mapper1
import fr.delphes.feature.DescriptorTestUtil.mapper2
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class MergeDescriptorRegistryTest {
    @Test
    internal fun `return descriptor`() {
        val registry = MergeDescriptorRegistry(
            DescriptorRegistry.of(mapper1),
            DescriptorRegistry.of(mapper2),
        )
        registry.descriptorOf(DescriptorTestUtil.Type1(), registry) shouldBe descriptor1
    }

    @Test
    internal fun `return the descriptor found`() {
        val registry = MergeDescriptorRegistry(
            DescriptorRegistry.of(mapper1),
            DescriptorRegistry.of(mapper1),
        )
        registry.descriptorOf(DescriptorTestUtil.Type1(), registry) shouldBe descriptor1
    }
}