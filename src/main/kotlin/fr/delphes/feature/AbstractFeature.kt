package fr.delphes.feature

import fr.delphes.event.eventHandler.EventHandler
import fr.delphes.event.incoming.MessageReceived
import fr.delphes.event.incoming.NewFollow
import fr.delphes.event.incoming.RewardRedemption
import fr.delphes.event.incoming.StreamOffline
import fr.delphes.event.incoming.StreamOnline
import fr.delphes.event.incoming.VIPListReceived

abstract class AbstractFeature : Feature {
    override val messageReceivedHandlers: List<EventHandler<MessageReceived>> = emptyList()
    override val rewardHandlers: List<EventHandler<RewardRedemption>> = emptyList()
    override val vipListReceivedHandlers: List<EventHandler<VIPListReceived>> = emptyList()
    override val newFollowHandlers: List<EventHandler<NewFollow>> = emptyList()
    override val streamOnlineHandlers: List<EventHandler<StreamOnline>> = emptyList()
    override val streamOfflineHandlers: List<EventHandler<StreamOffline>> = emptyList()
}