package fr.delphes.feature

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class FeaturePlatformTest {
    @Test
    internal fun `no runtime if no feature`() {
        runBlocking {
            val repository = TestFeatureRepository()

            val manager = FeaturePlatform(repository)
            manager.load()

            manager.runTimes.shouldBeEmpty()
        }
    }

    @Test
    internal fun `map runtime`() {
        runBlocking {
            val repository = TestFeatureRepository(FEATURE)

            val manager = FeaturePlatform(repository)
            manager.load()

            manager.runTimes.shouldContainExactly(FEATURE_RUNTIME)
        }
    }

    @Test
    internal fun `map only feature that can build runtime`() {
        runBlocking {
            val repository = TestFeatureRepository(FEATURE, FEATURE_WHICH_CAN_NOT_BUILD_RUNTIME)

            val manager = FeaturePlatform(repository)
            manager.load()

            manager.runTimes.shouldContainExactly(FEATURE_RUNTIME)
        }
    }

    @Test
    internal fun `failure don't make other features to fail`() {
        runBlocking {
            val repository = TestFeatureRepository(FEATURE, FEATURE_WITH_FAILURE)

            val manager = FeaturePlatform(repository)
            manager.load()

            manager.runTimes.shouldContainExactly(FEATURE_RUNTIME)
        }
    }

    companion object {
        private val FEATURE_RUNTIME = mockk<ExperimentalFeatureRuntime>()
        private val FEATURE = object : ExperimentalFeature<ExperimentalFeatureRuntime> {
            override val id = "feature"

            override fun buildRuntime() = FEATURE_RUNTIME
        }

        private val FEATURE_WHICH_CAN_NOT_BUILD_RUNTIME = object : ExperimentalFeature<ExperimentalFeatureRuntime> {
            override val id = "feature"

            override fun buildRuntime(): ExperimentalFeatureRuntime? = null
        }

        private val FEATURE_WITH_FAILURE = object : ExperimentalFeature<ExperimentalFeatureRuntime> {
            override val id = "feature"

            override fun buildRuntime(): ExperimentalFeatureRuntime? { throw RuntimeException("Something went wrong.") }
        }
    }
}

