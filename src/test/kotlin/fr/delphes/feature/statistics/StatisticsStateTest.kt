package fr.delphes.feature.statistics

import fr.delphes.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class StatisticsStateTest {
    @Nested
    inner class NumberOfChatters {
        @Test
        internal fun `one chatter`() {
            assertThat(
                StatisticsState(
                    mutableListOf(
                        UserMessage(User("user1"), "message user1"),
                        UserMessage(User("user1"), "second message user1")
                    )
                ).numberOfChatters
            ).isEqualTo(1)
        }

        @Test
        internal fun `two chatter`() {
            assertThat(
                StatisticsState(
                    mutableListOf(
                        UserMessage(User("user1"), "message user1"),
                        UserMessage(User("user2"), "second message user1")
                    )
                ).numberOfChatters
            ).isEqualTo(2)
        }
    }

    @Nested
    inner class NumberOfMessages {
        @Test
        internal fun `count message`() {
            assertThat(
                StatisticsState(
                    mutableListOf(
                        UserMessage(User("user1"), "message user1"),
                        UserMessage(User("user1"), "second message user1"),
                        UserMessage(User("user2"), "second message user1")
                    )
                ).numberMessages
            ).isEqualTo(3)
        }
    }
}