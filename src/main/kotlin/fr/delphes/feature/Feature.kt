package fr.delphes.feature

import fr.delphes.event.eventHandler.EventHandler
import fr.delphes.event.incoming.MessageReceived
import fr.delphes.event.incoming.NewFollow
import fr.delphes.event.incoming.RewardRedemption
import fr.delphes.event.incoming.StreamOffline
import fr.delphes.event.incoming.StreamOnline
import fr.delphes.event.incoming.VIPListReceived

interface Feature {
    val messageReceivedHandlers: List<EventHandler<MessageReceived>>
    val rewardHandlers: List<EventHandler<RewardRedemption>>
    val vipListReceivedHandlers: List<EventHandler<VIPListReceived>>
    val newFollowHandlers: List<EventHandler<NewFollow>>
    val streamOnlineHandlers: List<EventHandler<StreamOnline>>
    val streamOfflineHandlers: List<EventHandler<StreamOffline>>
}