package fr.delphes.features.twitch.command

import fr.delphes.descriptor.item.DurationDescriptor
import fr.delphes.descriptor.item.OutgoingEventListDescriptor
import fr.delphes.descriptor.item.StringDescriptor
import fr.delphes.descriptor.registry.DescriptorBuilder

val twitchCommandMapper = DescriptorBuilder.withItem<ExperimentalTwitchCommandConfiguration> { configuration, mapper ->
    listOf(
        StringDescriptor("id", configuration.id),
        StringDescriptor("channel", configuration.channel),
        StringDescriptor("trigger", configuration.trigger),
        DurationDescriptor("cooldown", configuration.cooldown),
        OutgoingEventListDescriptor.of("response", configuration.responses, mapper)
    )
}