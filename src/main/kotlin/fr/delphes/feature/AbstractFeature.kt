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

abstract class AbstractFeature : Feature {
    override val commands: Iterable<Command> = emptyList()
    override val messageReceivedHandlers: List<EventHandler<MessageReceived>> = emptyList()
    override val rewardHandlers: List<EventHandler<RewardRedemption>> = emptyList()
    override val vipListReceivedHandlers: List<EventHandler<VIPListReceived>> = emptyList()
    override val newFollowHandlers: List<EventHandler<NewFollow>> = emptyList()
    override val newSubHandlers: List<EventHandler<NewSub>> = emptyList()
    override val streamOnlineHandlers: List<EventHandler<StreamOnline>> = emptyList()
    override val streamOfflineHandlers: List<EventHandler<StreamOffline>> = emptyList()
}