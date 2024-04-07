package fr.delphes.dynamicForm

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage

class DynamicFormRegistryTest : ShouldSpec({
    should("register") {
        val registry = DynamicFormRegistry.of(
            DynamicFormRegistryEntry.of(
                "someForm",
                String::class,
                "tag"
            ),
            DynamicFormRegistryEntry.of(
                "otherForm",
                String::class,
                "otherTag"
            )
        )

        registry.entries shouldContain DynamicFormRegistryEntry.of(
            "someForm",
            String::class,
            "tag"
        )

        registry.entries shouldContain DynamicFormRegistryEntry.of(
            "otherForm",
            String::class,
            "otherTag"
        )
    }
    should("don't allow duplicate form name") {
        shouldThrow<IllegalArgumentException> {
            DynamicFormRegistry.of(
                DynamicFormRegistryEntry.of(
                    "duplicateName",
                    String::class,
                    "tag"
                ),
                DynamicFormRegistryEntry.of(
                    "duplicateName",
                    String::class,
                    "tag2"
                )
            )
        }.shouldHaveMessage("Duplicate form name: duplicateName")
    }
    should("don't allow duplicate form name (by composing multiple registries)") {
        shouldThrow<IllegalArgumentException> {
            DynamicFormRegistry.compose(
                DynamicFormRegistry.of(
                    DynamicFormRegistryEntry.of(
                        "duplicateName",
                        String::class,
                        "tag"
                    ),
                ),
                DynamicFormRegistry.of(
                    DynamicFormRegistryEntry.of(
                        "duplicateName",
                        String::class,
                        "tag2"
                    )
                ),
            )
        }.shouldHaveMessage("Duplicate form name: duplicateName")
    }
    should("don't find the form") {
        val registry = DynamicFormRegistry.empty()

        registry.find("notExistingForm").shouldBeNull()
    }
    should("find the form") {
        val registry = DynamicFormRegistry.of(
            DynamicFormRegistryEntry.of(
                "someForm",
                String::class,
                "tag"
            ),
        )

        registry.find("someForm") shouldBe DynamicFormRegistryEntry.of(
            "someForm",
            String::class,
            "tag"
        )
    }
    should("find by tag") {
        val registry = DynamicFormRegistry.of(
            DynamicFormRegistryEntry.of(
                "someForm",
                String::class,
                "tag"
            ),
            DynamicFormRegistryEntry.of(
                "otherFormWithSameTag",
                String::class,
                "tag"
            ),
        )

        registry.findByTag("tag").shouldContainExactlyInAnyOrder(
            DynamicFormRegistryEntry.of(
                "someForm",
                String::class,
                "tag"
            ),
            DynamicFormRegistryEntry.of(
                "otherFormWithSameTag",
                String::class,
                "tag"
            ),
        )
    }
})
