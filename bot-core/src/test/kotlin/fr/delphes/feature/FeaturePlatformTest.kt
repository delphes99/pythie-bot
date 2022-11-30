package fr.delphes.feature

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

class FeaturePlatformTest : ShouldSpec({
    should("no runtime if no feature") {
        runTest {
            val repository = TestFeatureRepository()

            val manager = FeaturePlatform(repository)
            manager.load()

            manager.runTimes.shouldBeEmpty()
        }
    }

    should("map runtime") {
        runTest {
            val repository = TestFeatureRepository(FEATURE)

            val manager = FeaturePlatform(repository)
            manager.load()

            manager.runTimes.shouldContainExactly(FEATURE_RUNTIME)
        }
    }

    should("map only feature that can build runtime") {
        runTest {
            val repository = TestFeatureRepository(FEATURE, FEATURE_WHICH_CAN_NOT_BUILD_RUNTIME)

            val manager = FeaturePlatform(repository)
            manager.load()

            manager.runTimes.shouldContainExactly(FEATURE_RUNTIME)
        }
    }

    should("failure don't make other features to fail") {
        runTest {
            val repository = TestFeatureRepository(FEATURE, FEATURE_WITH_FAILURE)

            val manager = FeaturePlatform(repository)
            manager.load()

            manager.runTimes.shouldContainExactly(FEATURE_RUNTIME)
        }
    }


}) {
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

            override fun buildRuntime(): ExperimentalFeatureRuntime? {
                throw RuntimeException("Something went wrong.")
            }
        }
    }
}

