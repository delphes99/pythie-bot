package fr.delphes.features

import fr.delphes.bot.event.outgoing.OutgoingEventBuilder
import fr.delphes.connector.twitch.builder.SendMessageBuilder
import fr.delphes.feature.FeatureDescription
import fr.delphes.features.core.botStarted.BotStartedDescription
import fr.delphes.features.discord.NewGuildMemberDescription
import fr.delphes.features.obs.SceneChangedDescription
import fr.delphes.features.obs.SourceFilterActivatedDescription
import fr.delphes.features.obs.SourceFilterDeactivatedDescription
import fr.delphes.features.overlay.OverlayDescription
import fr.delphes.features.twitch.bitCheer.BitCheerDescription
import fr.delphes.features.twitch.clipCreated.ClipCreatedDescription
import fr.delphes.features.twitch.command.CommandDescription
import fr.delphes.features.twitch.command.EditableCommandConfiguration
import fr.delphes.features.twitch.commandList.CommandListDescription
import fr.delphes.features.twitch.endCredits.EndCreditsDescription
import fr.delphes.features.twitch.gameDescription.GameDescriptionDescription
import fr.delphes.features.twitch.gameReward.GameRewardDescription
import fr.delphes.features.twitch.incomingRaid.IncomingRaidDescription
import fr.delphes.features.twitch.newFollow.NewFollowDescription
import fr.delphes.features.twitch.newSub.NewSubDescription
import fr.delphes.features.twitch.rewardRedeem.RewardRedeemDescription
import fr.delphes.features.twitch.statistics.StatisticsDescription
import fr.delphes.features.twitch.streamOffline.StreamOfflineDescription
import fr.delphes.features.twitch.streamOnline.StreamOnlineDescription
import fr.delphes.features.twitch.streamUpdate.StreamUpdateDescription
import fr.delphes.features.twitch.streamerHighlight.StreamerHighlightDescription
import fr.delphes.features.twitch.voth.VOTHDescription
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.serializer

//TODO split into connectors
object FeatureConfiguration {
    @InternalSerializationApi
    val serializersModule = SerializersModule {
        //Discord
        registerFeatureConfiguration<NewGuildMemberDescription>()
        // Overlay
        registerFeatureConfiguration<OverlayDescription>()
        //Twitch
        registerFeatureConfiguration<BitCheerDescription>()
        registerFeatureConfiguration<ClipCreatedDescription>()
        registerFeatureConfiguration<CommandDescription>()
        registerFeatureConfiguration<CommandListDescription>()
        registerFeatureConfiguration<EndCreditsDescription>()
        registerFeatureConfiguration<GameDescriptionDescription>()
        registerFeatureConfiguration<GameRewardDescription>()
        registerFeatureConfiguration<NewFollowDescription>()
        registerFeatureConfiguration<NewSubDescription>()
        registerFeatureConfiguration<RewardRedeemDescription>()
        registerFeatureConfiguration<StatisticsDescription>()
        registerFeatureConfiguration<StreamerHighlightDescription>()
        registerFeatureConfiguration<StreamOfflineDescription>()
        registerFeatureConfiguration<StreamOnlineDescription>()
        registerFeatureConfiguration<StreamUpdateDescription>()
        registerFeatureConfiguration<VOTHDescription>()
        registerFeatureConfiguration<IncomingRaidDescription>()
        registerFeatureConfiguration<EditableCommandConfiguration>()
        registerFeatureConfiguration<SceneChangedDescription>()
        registerFeatureConfiguration<SourceFilterActivatedDescription>()
        registerFeatureConfiguration<SourceFilterDeactivatedDescription>()
        registerFeatureConfiguration<BotStartedDescription>()

        registerOutgoingMessageBuilder<SendMessageBuilder>()
    }

    @InternalSerializationApi
    inline fun <reified T : FeatureDescription> SerializersModuleBuilder.registerFeatureConfiguration() {
        polymorphic(FeatureDescription::class, T::class, T::class.serializer())
    }

    @InternalSerializationApi
    inline fun <reified T : OutgoingEventBuilder> SerializersModuleBuilder.registerOutgoingMessageBuilder() {
        polymorphic(OutgoingEventBuilder::class, T::class, T::class.serializer())
    }
}