package fr.delphes.annotation.outgoingEvent.createBuilder

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import fr.delphes.feature.descriptor.FeatureDescriptor
import fr.delphes.feature.descriptor.StringFeatureDescriptor
import kotlin.reflect.KClass

sealed class FieldMetadata {
    abstract val name: String
    abstract val description: String
    abstract val descriptionClass: KClass<out FeatureDescriptor>
    abstract val fieldType: TypeName
    abstract val defaultValue: String
}

data class FieldWithType(
    override val name: String,
    override val description: String,
    val serializer: KClass<*>?,
    override val defaultValue: String,
    override val descriptionClass: KClass<out FeatureDescriptor>,
    override val fieldType: TypeName,
) : FieldMetadata()

data class FieldWithMapper(
    override val name: String,
    override val description: String,
    val mapperClass: KSType,
) : FieldMetadata() {
    override val descriptionClass = StringFeatureDescriptor::class
    override val fieldType = String::class.asTypeName()
    override val defaultValue = "\"\""
}