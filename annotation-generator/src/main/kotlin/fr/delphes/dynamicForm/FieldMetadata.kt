package fr.delphes.dynamicForm

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import fr.delphes.dynamicForm.descriptor.FieldDescriptor
import fr.delphes.dynamicForm.descriptor.StringFieldDescriptor
import kotlin.reflect.KClass

sealed class FieldMetadata {
    abstract val name: String
    abstract val description: String
    abstract val descriptionClass: KClass<out FieldDescriptor>
    abstract val fieldType: TypeName
    abstract val defaultValue: String
}

data class FieldWithType(
    override val name: String,
    override val description: String,
    val serializer: KClass<*>?,
    override val defaultValue: String,
    override val descriptionClass: KClass<out FieldDescriptor>,
    override val fieldType: TypeName,
) : FieldMetadata()

data class FieldWithMapper(
    override val name: String,
    override val description: String,
    val mapperClass: KSType,
) : FieldMetadata() {
    override val descriptionClass = StringFieldDescriptor::class
    override val fieldType = String::class.asTypeName()
    override val defaultValue = "\"\""
}