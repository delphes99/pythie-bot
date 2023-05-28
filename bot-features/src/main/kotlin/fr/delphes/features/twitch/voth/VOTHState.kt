package fr.delphes.features.twitch.voth

import fr.delphes.connector.twitch.incomingEvent.RewardRedemption
import fr.delphes.state.State
import fr.delphes.state.StateId
import fr.delphes.state.state.ClockState
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName
import kotlinx.coroutines.runBlocking

class VOTHState(
    val channel: TwitchChannel,
    savePath: String? = null,
    private val clock: ClockState,
) : State {
    override val id = idFor(channel)
    private val stateRepository = savePath?.let(::FileVOTHStateRepository)

    //TODO Externalize persistence / protect
    var state: VOTHStateData =
        stateRepository?.let { runBlocking { stateRepository.load() } } ?: VOTHStateData()

    internal val data get() = state

    val currentVip get() = state.currentVip

    suspend fun pause() {
        state = state.pause(clock.getValue())
        stateRepository?.save(state)
    }

    suspend fun unpause() {
        state = state.unpause(clock.getValue())
        stateRepository?.save(state)
    }

    suspend fun newVOTH(newVOTH: RewardRedemption) {
        state = state.newVOTH(newVOTH.user, newVOTH.cost, clock.getValue())
        stateRepository?.save(state)
    }

    fun getReignsFor(user: UserName): Stats {
        return state.getReignsFor(user, clock.getValue())
    }

    fun getTopVip(numberOfTopVip: Int): List<Stats> {
        return state.topVip(numberOfTopVip, clock.getValue())
    }

    fun lastReigns(): List<VOTHReign> {
        return state.lastReigns(clock.getValue())
    }

    companion object {
        fun idFor(channel: TwitchChannel) = StateId.from<VOTHState>("voth-${channel.name}")
    }
}