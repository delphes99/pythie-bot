package fr.delphes.feature.voth

import fr.delphes.twitch.model.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime

internal class VOTHStateTest {
    private val NOW = LocalDateTime.of(2020, 7, 1, 10, 0)
    private val USER = User("user")

    @Test
    internal fun `list previous reigns for user`() {
        val reign = VOTHReign(USER, Duration.ofMinutes(5), 50)
        val state =
            VOTHState(previousReigns = mutableListOf(reign))

        assertThat(state.getReignsFor(USER, NOW)).isEqualTo(
            Stats(USER, reign)
        )
    }

    @Test
    internal fun `list previous reigns and current reign`() {
        val previousReign = VOTHReign(USER, Duration.ofMinutes(5), 50)
        val currentReign = VOTHWinner(USER, NOW.minusMinutes(15), 25)
        val state =
            VOTHState(
                currentVip = currentReign,
                previousReigns = mutableListOf(previousReign)
            )

        assertThat(state.getReignsFor(USER, NOW)).isEqualTo(
            Stats(
                USER,
                previousReign,
                VOTHReign(USER, Duration.ofMinutes(15), 25)
            )
        )
    }

    @Test
    internal fun `get top 3`() {
        val user1 = User("user1")
        val user2 = User("user2")
        val user3 = User("user3")

        val currentReign = VOTHWinner(user1, NOW.minusMinutes(15), 25)
        val previousReignForUser1 = listOf(
            VOTHReign(user1, Duration.ofMinutes(45), 50)
        )
        val previousReignForUser2 = listOf(
            VOTHReign(user2, Duration.ofMinutes(15), 50),
            VOTHReign(user2, Duration.ofMinutes(15), 50)
        )
        val previousReignForUser3 = listOf(
            VOTHReign(user3, Duration.ofMinutes(25), 50)
        )
        val state =
            VOTHState(
                currentVip = currentReign,
                previousReigns = previousReignForUser1.plus(previousReignForUser2).plus(previousReignForUser3)
                    .toMutableList()
            )

        assertThat(state.top3(NOW)).isEqualTo(
            listOf(
                Stats(
                    user1,
                    *previousReignForUser1.toTypedArray(),
                    VOTHReign(user1, Duration.ofMinutes(15), 25)
                ),
                Stats(
                    user2,
                    *previousReignForUser2.toTypedArray()
                ),
                Stats(
                    user3,
                    *previousReignForUser3.toTypedArray()
                )
            )
        )
    }

    @Test
    internal fun `freeze current reign when pause`() {
        val previousReign = VOTHReign(USER, Duration.ofMinutes(5), 50)
        val currentReignCost = 25L
        val currentReign = VOTHWinner(USER, NOW.minusMinutes(15), currentReignCost)
        val state =
            VOTHState(
                currentVip = currentReign,
                previousReigns = mutableListOf(previousReign)
            )

        state.pause(NOW)

        assertThat(state.currentVip).isEqualTo(
            VOTHWinner("user", null, currentReignCost, listOf(Duration.ofMinutes(15)))
        )
    }

    @Test
    internal fun `restart current reign from now when unpause`() {
        val previousReign = VOTHReign(USER, Duration.ofMinutes(5), 50)
        val currentReignCost = 25L
        val currentReign = VOTHWinner(USER, NOW.minusMinutes(15), currentReignCost)
        val state =
            VOTHState(
                currentVip = currentReign,
                previousReigns = mutableListOf(previousReign)
            )

        state.pause(NOW)
        state.unpause(NOW.plusMinutes(5))

        assertThat(state.currentVip).isEqualTo(
            VOTHWinner(
                "user",
                NOW.plusMinutes(5),
                currentReignCost,
                listOf(
                    Duration.ofMinutes(15)
                )
            )
        )
    }

    @Test
    internal fun `multiple pause and unpause`() {
        val previousReign = VOTHReign(USER, Duration.ofMinutes(5), 50)
        val currentReignCost = 25L
        val currentReign = VOTHWinner(USER, NOW.minusMinutes(15), currentReignCost)
        val state =
            VOTHState(
                currentVip = currentReign,
                previousReigns = mutableListOf(previousReign)
            )

        state.pause(NOW)
        state.unpause(NOW.plusMinutes(5))
        state.pause(NOW.plusMinutes(65))
        state.unpause(NOW.plusMinutes(245))

        assertThat(state.currentVip).isEqualTo(
            VOTHWinner(
                "user",
                NOW.plusMinutes(245),
                currentReignCost,
                listOf(
                    Duration.ofMinutes(15),
                    Duration.ofMinutes(60)
                )
            )
        )
    }

    @Test
    internal fun `last reigns`() {
        val previousReign1 = VOTHReign(USER, Duration.ofMinutes(5), 50)
        val previousReign2 = VOTHReign(User("user2"), Duration.ofMinutes(5), 50)
        val previousReign3 = VOTHReign(USER, Duration.ofMinutes(5), 50)
        val currentReign = VOTHWinner(USER, NOW.minusMinutes(15), 25)

        val state =
            VOTHState(
                currentVip = currentReign,
                previousReigns = mutableListOf(
                    previousReign1,
                    previousReign2,
                    previousReign3,
                )
            )

        assertThat(state.lastReigns(NOW)).containsExactly(
            VOTHReign(USER, Duration.ofMinutes(15), 25),
            previousReign3,
            previousReign2,
            previousReign1
        )
    }
}