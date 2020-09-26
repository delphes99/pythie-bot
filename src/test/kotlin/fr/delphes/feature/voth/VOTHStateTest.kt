package fr.delphes.feature.voth

import fr.delphes.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime

internal class VOTHStateTest {
    val SOME_DATE = LocalDateTime.of(2020, 1, 1, 10, 0)
    val NOW = LocalDateTime.of(2020, 7, 1, 10, 0)

    @Test
    internal fun `list previous reigns`() {
        val reign = VOTHReign(User("user"), Duration.ofMinutes(5), 50)
        val state =
            VOTHState(previousReigns = mutableListOf(reign))

        assertThat(state.getReignsFor(User("user"), NOW)).isEqualTo(
            Stats(reign)
        )
    }

    @Test
    internal fun `list previous reigns and current reign`() {
        val previousReign = VOTHReign(User("user"), Duration.ofMinutes(5), 50)
        val currentReign = VOTHWinner(User("user"), NOW.minusMinutes(15), 25)
        val state =
            VOTHState(
                currentVip = currentReign,
                previousReigns = mutableListOf(previousReign)
            )

        assertThat(state.getReignsFor(User("user"), NOW)).isEqualTo(
            Stats(
                previousReign,
                VOTHReign(User("user"), Duration.ofMinutes(15), 25)
            )
        )
    }

    @Test
    internal fun `freeze current reign when pause`() {
        val previousReign = VOTHReign(User("user"), Duration.ofMinutes(5), 50)
        val currentReignCost = 25L
        val currentReign = VOTHWinner(User("user"), NOW.minusMinutes(15), currentReignCost)
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
        val previousReign = VOTHReign(User("user"), Duration.ofMinutes(5), 50)
        val currentReignCost = 25L
        val currentReign = VOTHWinner(User("user"), NOW.minusMinutes(15), currentReignCost)
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
        val previousReign = VOTHReign(User("user"), Duration.ofMinutes(5), 50)
        val currentReignCost = 25L
        val currentReign = VOTHWinner(User("user"), NOW.minusMinutes(15), currentReignCost)
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
}