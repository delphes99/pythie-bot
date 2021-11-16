package fr.delphes.bot.state

import fr.delphes.twitch.api.user.User
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class StatisticsTest {
    private val USER_1 = User("user1")
    private val USER_2 = User("user2")

    @Nested
    inner class NumberOfChatters {
        @Test
        internal fun `one chatter`() {
            Statistics(
                mutableListOf(
                    UserMessage(USER_1, "message user1"),
                    UserMessage(USER_1, "second message user1")
                )
            ).numberOfChatters shouldBe 1
        }

        @Test
        internal fun `two chatter`() {
            Statistics(
                mutableListOf(
                    UserMessage(USER_1, "message user1"),
                    UserMessage(USER_2, "second message user1")
                )
            ).numberOfChatters shouldBe 2
        }
    }

    @Nested
    inner class NumberOfMessages {
        @Test
        internal fun `count message`() {
            Statistics(
                mutableListOf(
                    UserMessage(USER_1, "message user1"),
                    UserMessage(USER_1, "second message user1"),
                    UserMessage(USER_2, "second message user1")
                )
            ).numberMessages shouldBe 3
        }
    }

    @Nested
    inner class NewFollow {
        @Test
        internal fun `new follow`() {
            val state = Statistics()
            state.newFollow(USER_1)

            state.numberOfFollow shouldBe 1
        }

        @Test
        internal fun `don't increment new follow twice for the user`() {
            val state = Statistics()
            state.newFollow(USER_1)
            state.newFollow(USER_1)

            state.numberOfFollow shouldBe 1
        }
    }

    @Nested
    inner class NewSub {
        @Test
        internal fun `new sub`() {
            val state = Statistics()
            state.newSub(USER_1)

            state.numberOfSub shouldBe 1
        }

        @Test
        internal fun `don't increment new sub twice for the user`() {
            val state = Statistics()
            state.newSub(USER_1)
            state.newSub(USER_1)

            state.numberOfSub shouldBe 1
        }
    }
}