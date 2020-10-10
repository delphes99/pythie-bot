package fr.delphes.feature

import fr.delphes.bot.command.Command
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.incoming.MessageReceived
import fr.delphes.bot.event.incoming.NewFollow
import fr.delphes.bot.event.incoming.NewSub
import fr.delphes.bot.event.incoming.RewardRedemption
import fr.delphes.bot.event.incoming.StreamOffline
import fr.delphes.bot.event.incoming.StreamOnline
import fr.delphes.bot.event.incoming.VIPListReceived

interface Feature {
    val commands: Iterable<Command>
    val messageReceivedHandlers: List<EventHandler<MessageReceived>>
    val rewardHandlers: List<EventHandler<RewardRedemption>>
    val vipListReceivedHandlers: List<EventHandler<VIPListReceived>>
    val newFollowHandlers: List<EventHandler<NewFollow>>
    val newSubHandlers: List<EventHandler<NewSub>>
    val streamOnlineHandlers: List<EventHandler<StreamOnline>>
    val streamOfflineHandlers: List<EventHandler<StreamOffline>>
}