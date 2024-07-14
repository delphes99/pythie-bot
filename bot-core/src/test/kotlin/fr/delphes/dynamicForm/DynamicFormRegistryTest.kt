package fr.delphes.dynamicForm

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.Tuple3
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.mockk

class DynamicFormRegistryTest : ShouldSpec({
    should("register") {
        val registry = DynamicFormRegistry.of(DynamicFormRegistryEntry.of(
            DynamicFormType("someForm"), String::class, listOf("tag"),
            {
                mockk<DynamicFormDTO<String>>()
            }, {
                mockk<DynamicFormDTO<String>>()
            }
        ), DynamicFormRegistryEntry.of(
            DynamicFormType("otherForm"), String::class, listOf("otherTag"),
            {
                mockk<DynamicFormDTO<String>>()
            }, {
                mockk<DynamicFormDTO<String>>()
            }
        )
        )

        registry.entries.map { Tuple3(it.type, it.clazz, it.tags) } shouldContain Tuple3(
            DynamicFormType("someForm"), String::class, listOf("tag")
        )

        registry.entries.map { Tuple3(it.type, it.clazz, it.tags) } shouldContain Tuple3(
            DynamicFormType("otherForm"), String::class, listOf("otherTag")
        )
    }
    should("don't allow duplicate form name") {
        shouldThrow<IllegalArgumentException> {
            DynamicFormRegistry.of(
                DynamicFormRegistryEntry.of(
                    DynamicFormType("duplicateName"), String::class, listOf("tag"),
                    {
                        mockk<DynamicFormDTO<String>>()
                    }, {
                        mockk<DynamicFormDTO<String>>()
                    }
                ), DynamicFormRegistryEntry.of(
                    DynamicFormType("duplicateName"), String::class, listOf("tag2"),
                    {
                        mockk<DynamicFormDTO<String>>()
                    }, {
                        mockk<DynamicFormDTO<String>>()
                    }
                )
            )
        }.shouldHaveMessage("Duplicate form name: duplicateName")
    }
    should("don't allow duplicate form name (by composing multiple registries)") {
        shouldThrow<IllegalArgumentException> {
            DynamicFormRegistry.compose(
                DynamicFormRegistry.of(
                    DynamicFormRegistryEntry.of(
                        DynamicFormType("duplicateName"), String::class, listOf("tag"),
                        {
                            mockk<DynamicFormDTO<String>>()
                        }, {
                            mockk<DynamicFormDTO<String>>()
                        }
                    ),
                ),
                DynamicFormRegistry.of(
                    DynamicFormRegistryEntry.of(
                        DynamicFormType("duplicateName"), String::class, listOf("tag2"),
                        {
                            mockk<DynamicFormDTO<String>>()
                        }, {
                            mockk<DynamicFormDTO<String>>()
                        }
                    )
                ),
            )
        }.shouldHaveMessage("Duplicate form name: duplicateName")
    }
    should("don't find the form") {
        val registry = DynamicFormRegistry.empty()

        registry.find(DynamicFormType("notExistingForm")).shouldBeNull()
    }
    should("find the form") {
        val registry = DynamicFormRegistry.of(
            DynamicFormRegistryEntry.of(
                DynamicFormType("someForm"), String::class, listOf("tag"),
                {
                    mockk<DynamicFormDTO<String>>()
                }, {
                    mockk<DynamicFormDTO<String>>()
                }
            ),
        )

        registry.find(DynamicFormType("someForm"))
            .shouldNotBeNull()
            .should {
                it.type shouldBe DynamicFormType("someForm")
                it.clazz shouldBe String::class
                it.tags shouldBe listOf("tag")
            }
    }
    should("find by tag") {
        val registry = DynamicFormRegistry.of(
            DynamicFormRegistryEntry.of(
                DynamicFormType("someForm"), String::class, listOf("tag"),
                {
                    mockk<DynamicFormDTO<String>>()
                }, {
                    mockk<DynamicFormDTO<String>>()
                }
            ),
            DynamicFormRegistryEntry.of(
                DynamicFormType("otherFormWithSameTag"), String::class, listOf("tag"),
                {
                    mockk<DynamicFormDTO<String>>()
                }, {
                    mockk<DynamicFormDTO<String>>()
                }
            ),
        )

        registry.findByTag("tag").map { Tuple3(it.type, it.clazz, it.tags) }.shouldContainExactlyInAnyOrder(
            Tuple3(DynamicFormType("someForm"), String::class, listOf("tag")),
            Tuple3(DynamicFormType("otherFormWithSameTag"), String::class, listOf("tag")),
        )
    }
    should("create empty form from type") {
        val type = DynamicFormType("someForm")
        val registry = DynamicFormRegistry.of(
            DynamicFormRegistryEntry.of(
                type, String::class, listOf("tag"),
                {
                    mockk<DynamicFormDTO<String>>() {
                        every { build() } returns "build from build()"
                    }
                }, {
                    mockk<DynamicFormDTO<String>>()
                }
            ),
        )

        registry.newInstanceOf(type).shouldBe("build from build()")
    }
})