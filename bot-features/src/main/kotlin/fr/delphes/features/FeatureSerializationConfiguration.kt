package fr.delphes.features

import fr.delphes.bot.event.outgoing.OutgoingEventBuilder
import fr.delphes.connector.twitch.builder.SendMessageBuilder
import fr.delphes.feature.FeatureDescription
import fr.delphes.feature.featureNew.FeatureConfiguration
import fr.delphes.feature.featureNew.FeatureState
import fr.delphes.features.core.botStarted.BotStartedDescription
import fr.delphes.features.discord.NewGuildMemberDescription
import fr.delphes.features.obs.SceneChangedDescription
import fr.delphes.features.obs.SourceFilterActivatedDescription
import fr.delphes.features.obs.SourceFilterDeactivatedDescription
import fr.delphes.features.overlay.OverlayDescription
import fr.delphes.features.twitch.NewTwitchCommand
import fr.delphes.features.twitch.NewTwitchCommandState
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
object FeatureSerializationConfiguration {
    @InternalSerializationApi
    val serializersModule = SerializersModule {
        //Discord
        registerLegacyFeatureConfiguration<NewGuildMemberDescription>()
        // Overlay
        registerLegacyFeatureConfiguration<OverlayDescription>()
        //Twitch
        registerLegacyFeatureConfiguration<BitCheerDescription>()
        registerLegacyFeatureConfiguration<ClipCreatedDescription>()
        registerLegacyFeatureConfiguration<CommandDescription>()
        registerLegacyFeatureConfiguration<CommandListDescription>()
        registerLegacyFeatureConfiguration<EndCreditsDescription>()
        registerLegacyFeatureConfiguration<GameDescriptionDescription>()
        registerLegacyFeatureConfiguration<GameRewardDescription>()
        registerLegacyFeatureConfiguration<NewFollowDescription>()
        registerLegacyFeatureConfiguration<NewSubDescription>()
        registerLegacyFeatureConfiguration<RewardRedeemDescription>()
        registerLegacyFeatureConfiguration<StatisticsDescription>()
        registerLegacyFeatureConfiguration<StreamerHighlightDescription>()
        registerLegacyFeatureConfiguration<StreamOfflineDescription>()
        registerLegacyFeatureConfiguration<StreamOnlineDescription>()
        registerLegacyFeatureConfiguration<StreamUpdateDescription>()
        registerLegacyFeatureConfiguration<VOTHDescription>()
        registerLegacyFeatureConfiguration<IncomingRaidDescription>()
        registerLegacyFeatureConfiguration<EditableCommandConfiguration>()
        registerLegacyFeatureConfiguration<SceneChangedDescription>()
        registerLegacyFeatureConfiguration<SourceFilterActivatedDescription>()
        registerLegacyFeatureConfiguration<SourceFilterDeactivatedDescription>()
        registerLegacyFeatureConfiguration<BotStartedDescription>()

        registerOutgoingMessageBuilder<SendMessageBuilder>()

        registerFeatureConfiguration<NewTwitchCommand>()
        registerFeatureState<NewTwitchCommandState>()
    }

    @InternalSerializationApi
    inline fun <reified T : FeatureDescription> SerializersModuleBuilder.registerLegacyFeatureConfiguration() {
        polymorphic(FeatureDescription::class, T::class, T::class.serializer())
    }

    @InternalSerializationApi
    inline fun <reified T : OutgoingEventBuilder> SerializersModuleBuilder.registerOutgoingMessageBuilder() {
        polymorphic(OutgoingEventBuilder::class, T::class, T::class.serializer())
    }

    @InternalSerializationApi
    inline fun <reified T : FeatureConfiguration<out FeatureState>> SerializersModuleBuilder.registerFeatureConfiguration() {
        polymorphic(FeatureConfiguration::class, T::class, T::class.serializer())
    }

    @InternalSerializationApi
    inline fun <reified T : FeatureState> SerializersModuleBuilder.registerFeatureState() {
        polymorphic(FeatureState::class, T::class, T::class.serializer())
    }
}