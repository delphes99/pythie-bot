package fr.delphes.features

import fr.delphes.feature.FeatureDescription
import fr.delphes.features.discord.NewGuildMemberDescription
import fr.delphes.features.overlay.OverlayDescription
import fr.delphes.features.twitch.bitCheer.BitCheerDescription
import fr.delphes.features.twitch.clipCreated.ClipCreatedDescription
import fr.delphes.features.twitch.command.CommandDescription
import fr.delphes.features.twitch.commandList.CommandListDescription
import fr.delphes.features.twitch.endCredits.EndCreditsDescription
import fr.delphes.features.twitch.gameDescription.GameDescriptionDescription
import fr.delphes.features.twitch.gameReward.GameRewardDescription
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
    }

    @InternalSerializationApi
    inline fun <reified T: FeatureDescription> SerializersModuleBuilder.registerFeatureConfiguration() {
        polymorphic(FeatureDescription::class, T::class, T::class.serializer())
    }
}