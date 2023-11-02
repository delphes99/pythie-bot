package fr.delphes.annotation.outgoingEvent.createBuilder

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName
import fr.delphes.feature.OutgoingEventBuilderDescription
import fr.delphes.feature.descriptor.DurationFeatureDescriptor
import fr.delphes.feature.descriptor.FeatureDescriptor
import fr.delphes.feature.descriptor.StringFeatureDescriptor
import fr.delphes.utils.serialization.DurationSerializer
import kotlin.reflect.KClass

class FieldInfos(
    val descriptionClass: KClass<out FeatureDescriptor>,
    val mapperClass: KSType? = null,
) {
    val defaultValue: String
        get() = when (descriptionClass) {
            StringFeatureDescriptor::class -> "\"\""
            DurationFeatureDescriptor::class -> "Duration.ZERO"
            OutgoingEventBuilderDescription::class -> "listOf()"
            else -> error("Unknown default value for $descriptionClass")
        }

    val fieldSerializer: ClassName?
        get() = when (descriptionClass) {
            DurationFeatureDescriptor::class -> DurationSerializer::class.asTypeName()
            StringFeatureDescriptor::class,
            OutgoingEventBuilderDescription::class,
            -> null

            else -> error("Unknown serializer for $descriptionClass")
        }

    companion object {
        fun of(fieldMapper: KSType): FieldInfos {
            return FieldInfos(
                StringFeatureDescriptor::class,
                fieldMapper
            )
        }
    }
}