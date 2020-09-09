package fr.delphes.feature.voth

import fr.delphes.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class VOTHStateTest {
    val SOME_DATE = LocalDateTime.of(2020, 1, 1, 10, 0)
    val NOW = LocalDateTime.of(2020, 7, 1, 10, 0)

    @Test
    internal fun `list previous reigns`() {
        val reign = VOTHReign(User("user"), SOME_DATE, SOME_DATE.plusMinutes(5), 50)
        val state =
            VOTHState(previousReigns = mutableListOf(reign))

        assertThat(state.getReignsFor(User("user"), NOW)).isEqualTo(
            Stats(reign)
        )
    }

    @Test
    internal fun `list previous reigns and current reign`() {
        val previousReign = VOTHReign(User("user"), SOME_DATE, SOME_DATE.plusMinutes(5), 50)
        val currentReign = VOTHWinner(User("user"), NOW.minusMinutes(15), 25)
        val state =
            VOTHState(
                currentVip = currentReign,
                previousReigns = mutableListOf(previousReign)
            )

        assertThat(state.getReignsFor(User("user"), NOW)).isEqualTo(
            Stats(
                previousReign,
                VOTHReign(User("user"), NOW.minusMinutes(15), NOW, 25)
            )
        )
    }
}