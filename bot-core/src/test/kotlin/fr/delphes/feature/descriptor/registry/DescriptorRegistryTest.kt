package fr.delphes.feature.descriptor.registry

import fr.delphes.descriptor.registry.DescriptorRegistry
import fr.delphes.descriptor.registry.DescriptorRegistryEntry
import fr.delphes.feature.DescriptorTestUtil
import fr.delphes.feature.DescriptorTestUtil.descriptor1
import fr.delphes.feature.DescriptorTestUtil.descriptorType1
import fr.delphes.feature.DescriptorTestUtil.descriptorType2
import fr.delphes.feature.DescriptorTestUtil.emptyDescriptorRegistry
import fr.delphes.feature.DescriptorTestUtil.mapper1
import fr.delphes.feature.DescriptorTestUtil.mapper2
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class DescriptorRegistryTest {
    @Test
    internal fun `find entry by type`() {
        val registry = DescriptorRegistry.of(mapper1)

        registry.getEntry(descriptorType1) shouldBe DescriptorRegistryEntry(descriptorType1, mapper1)
    }

    @Test
    internal fun `map to descriptor`() {
        val registry = DescriptorRegistry.of(mapper1, mapper2)

        registry.descriptorOf(DescriptorTestUtil.Type1(), emptyDescriptorRegistry) shouldBe descriptor1
    }

    @Test
    internal fun `no descriptor for unregistered type`() {
        val registry = DescriptorRegistry.of<Any>(mapper1)

        registry.descriptorOf(DescriptorTestUtil.Type2(), emptyDescriptorRegistry).shouldBeNull()
    }

    @Test
    internal fun `get all descriptor types registered`() {
        val registry = DescriptorRegistry.of(mapper1, mapper2)

        registry.getRegisteredTypes().shouldContainExactlyInAnyOrder(
            descriptorType1,
            descriptorType2
        )
    }
}