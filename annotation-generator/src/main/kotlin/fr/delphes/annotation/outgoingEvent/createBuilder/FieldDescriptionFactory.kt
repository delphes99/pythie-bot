package fr.delphes.annotation.outgoingEvent.createBuilder

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ksp.toClassName

object FieldDescriptionFactory {
    fun buildDescription(builder: FunSpec.Builder, property: FieldMetadata) {
        with(builder) {
            addCode("%T(", property.descriptionClass)
            addStatement("fieldName=\"${property.name}\",")
            addStatement("description=\"${property.description}\",")
            addStatement("value=${property.name},")
            addCode("),\n")
        }
    }

    fun buildEncodeValue(builder: FunSpec.Builder, property: FieldMetadata, stateProviderName: String) {
        when (property) {
            is FieldWithMapper -> {
                builder.addCode(
                    "%T.map(this.${property.name}, $stateProviderName)", property.mapperClass.toClassName()
                )
            }

            is FieldWithType -> {
                builder.addCode("this.${property.name}")
            }
        }
    }
}