package fr.delphes.feature.statistics

import fr.delphes.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class StatisticsStateTest {
    private val USER_1 = User("user1")
    private val USER_2 = User("user2")

    @Nested
    inner class NumberOfChatters {
        @Test
        internal fun `one chatter`() {
            assertThat(
                StatisticsState(
                    mutableListOf(
                        UserMessage(USER_1, "message user1"),
                        UserMessage(USER_1, "second message user1")
                    )
                ).numberOfChatters
            ).isEqualTo(1)
        }

        @Test
        internal fun `two chatter`() {
            assertThat(
                StatisticsState(
                    mutableListOf(
                        UserMessage(USER_1, "message user1"),
                        UserMessage(USER_2, "second message user1")
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
                        UserMessage(USER_1, "message user1"),
                        UserMessage(USER_1, "second message user1"),
                        UserMessage(USER_2, "second message user1")
                    )
                ).numberMessages
            ).isEqualTo(3)
        }
    }

    @Nested
    inner class NewFollow {
        @Test
        internal fun `new follow`() {
            val state = StatisticsState()
            state.newFollow(USER_1)

            assertThat(state.numberOfFollow).isEqualTo(1)
        }

        @Test
        internal fun `don't increment new follow twice for the user`() {
            val state = StatisticsState()
            state.newFollow(USER_1)
            state.newFollow(USER_1)

            assertThat(state.numberOfFollow).isEqualTo(1)
        }
    }

    @Nested
    inner class NewSub {
        @Test
        internal fun `new sub`() {
            val state = StatisticsState()
            state.newSub(USER_1)

            assertThat(state.numberOfSub).isEqualTo(1)
        }

        @Test
        internal fun `don't increment new sub twice for the user`() {
            val state = StatisticsState()
            state.newSub(USER_1)
            state.newSub(USER_1)

            assertThat(state.numberOfSub).isEqualTo(1)
        }
    }
}