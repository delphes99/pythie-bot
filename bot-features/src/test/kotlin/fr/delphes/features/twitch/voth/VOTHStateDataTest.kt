package fr.delphes.features.twitch.voth

import fr.delphes.twitch.api.user.UserName
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import java.time.Duration
import java.time.LocalDateTime

class VOTHStateDataTest : ShouldSpec({
    should("list previous reigns for user") {
        val reign = VOTHReign(USER, Duration.ofMinutes(5), 50)
        val state =
            VOTHStateData(previousReigns = listOf(reign))

        state.getReignsFor(USER, NOW) shouldBe Stats(USER, reign)
    }

    should("list previous reigns and current reign") {
        val previousReign = VOTHReign(USER, Duration.ofMinutes(5), 50)
        val currentReign = VOTHWinner(USER, NOW.minusMinutes(15), 25)
        val state =
            VOTHStateData(
                currentVip = currentReign,
                previousReigns = listOf(previousReign)
            )

        state.getReignsFor(USER, NOW) shouldBe Stats(
            USER,
            previousReign,
            VOTHReign(USER, Duration.ofMinutes(15), 25)
        )

    }

    should("get top 3") {
        val user1 = UserName("user1")
        val user2 = UserName("user2")
        val user3 = UserName("user3")

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
            VOTHStateData(
                currentVip = currentReign,
                previousReigns = previousReignForUser1.plus(previousReignForUser2).plus(previousReignForUser3)
            )

        state.topVip(3, NOW) shouldBe listOf(
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
    }

    should("freeze current reign when pause") {
        val previousReign = VOTHReign(USER, Duration.ofMinutes(5), 50)
        val currentReignCost = 25L
        val currentReign = VOTHWinner(USER, NOW.minusMinutes(15), currentReignCost)
        val state =
            VOTHStateData(
                currentVip = currentReign,
                previousReigns = listOf(previousReign)
            )

        val newState = state.pause(NOW)

        newState.currentVip shouldBe VOTHWinner("user", null, currentReignCost, listOf(Duration.ofMinutes(15)))
    }

    should("restart current reign from now when unpause") {
        val previousReign = VOTHReign(USER, Duration.ofMinutes(5), 50)
        val currentReignCost = 25L
        val currentReign = VOTHWinner(USER, NOW.minusMinutes(15), currentReignCost)
        val state =
            VOTHStateData(
                currentVip = currentReign,
                previousReigns = listOf(previousReign)
            ).pause(NOW)

        val newState = state.unpause(NOW.plusMinutes(5))

        newState.currentVip shouldBe VOTHWinner(
            "user",
            NOW.plusMinutes(5),
            currentReignCost,
            listOf(
                Duration.ofMinutes(15)
            )
        )
    }

    should("multiple pause and unpause") {
        val previousReign = VOTHReign(USER, Duration.ofMinutes(5), 50)
        val currentReignCost = 25L
        val currentReign = VOTHWinner(USER, NOW.minusMinutes(15), currentReignCost)
        var state =
            VOTHStateData(
                currentVip = currentReign,
                previousReigns = listOf(previousReign)
            )

        state = state.pause(NOW)
        state = state.unpause(NOW.plusMinutes(5))
        state = state.pause(NOW.plusMinutes(65))
        state = state.unpause(NOW.plusMinutes(245))

        state.currentVip shouldBe VOTHWinner(
            "user",
            NOW.plusMinutes(245),
            currentReignCost,
            listOf(
                Duration.ofMinutes(15),
                Duration.ofMinutes(60)
            )
        )
    }

    should("last reigns") {
        val previousReign1 = VOTHReign(USER, Duration.ofMinutes(5), 50)
        val previousReign2 = VOTHReign(UserName("user2"), Duration.ofMinutes(5), 50)
        val previousReign3 = VOTHReign(USER, Duration.ofMinutes(5), 50)
        val currentReign = VOTHWinner(USER, NOW.minusMinutes(15), 25)

        val state =
            VOTHStateData(
                currentVip = currentReign,
                previousReigns = listOf(
                    previousReign1,
                    previousReign2,
                    previousReign3,
                )
            )

        state.lastReigns(NOW).shouldContainExactly(
            VOTHReign(USER, Duration.ofMinutes(15), 25),
            previousReign3,
            previousReign2,
            previousReign1
        )
    }

    should("no change if new VIP is the current vip") {
        val reign = VOTHReign(USER, Duration.ofMinutes(5), 50)
        val currentReign = VOTHWinner(USER, NOW.minusMinutes(15), 25)

        val state =
            VOTHStateData(
                currentVip = currentReign,
                previousReigns = listOf(
                    reign
                )
            )

        state.newVOTH(USER, 25, NOW) shouldBe state
    }
}) {
    companion object {
        private val NOW = LocalDateTime.of(2020, 7, 1, 10, 0)
        private val USER = UserName("user")
    }
}