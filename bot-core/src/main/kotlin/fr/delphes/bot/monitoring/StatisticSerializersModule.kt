import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.monitoring.StatisticData
import fr.delphes.bot.monitoring.StatisticIncomingEventData
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

val statisticSerializerModule = SerializersModule {
    polymorphic(StatisticData::class) {
        subclass(StatisticIncomingEventData.serializer(PolymorphicSerializer(IncomingEvent::class)))
    }
}