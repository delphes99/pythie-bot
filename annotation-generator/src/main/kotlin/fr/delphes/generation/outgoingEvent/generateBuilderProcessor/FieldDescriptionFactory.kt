package fr.delphes.generation.outgoingEvent.generateBuilderProcessor

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ksp.toClassName
import fr.delphes.generation.dynamicForm.metada.FieldMetadata
import fr.delphes.generation.dynamicForm.metada.FieldWithFormList
import fr.delphes.generation.dynamicForm.metada.FieldWithMapper
import fr.delphes.generation.dynamicForm.metada.FieldWithType

object FieldDescriptionFactory {
    fun buildDescription(builder: FunSpec.Builder, property: FieldMetadata) {
        with(builder) {
            when (property) {
                is FieldWithType,
                is FieldWithMapper,
                -> {
                    addCode("%T(", property.descriptionClass)
                    addStatement("fieldName=\"${property.name}\",")
                    addStatement("description=\"${property.description}\",")
                    addStatement("value=${property.name},")
                    addCode("),\n")
                }

                is FieldWithFormList -> {
                    addCode("%T(", property.descriptionClass)
                    addStatement("fieldName=\"${property.name}\",")
                    addStatement("description=\"${property.description}\",")
                    addStatement("formFamily=\"${property.formFamily}\",")
                    addStatement("value=${property.name},")
                    addCode("),\n")
                }
            }
        }
    }

    fun buildDtoToObject(property: FieldMetadata): CodeBlock {
        val builder = CodeBlock.builder()
        when (property) {
            is FieldWithMapper -> {
                builder.addStatement(
                    "%T.mapFromDto(this.${property.name})", property.mapperClass.toClassName()
                )
            }

            is FieldWithType -> {
                builder.addStatement("this.${property.name}")
            }

            is FieldWithFormList -> {
                //TODO
                builder.addStatement("emptyList()")
            }
        }
        return builder.build()
    }

    fun buildObjectToDto(property: FieldMetadata, variableName: String = "this"): CodeBlock {
        val builder = CodeBlock.builder()
        when (property) {
            is FieldWithMapper -> {
                builder.addStatement(
                    "%T.mapToDto($variableName.${property.name})", property.mapperClass.toClassName()
                )
            }

            is FieldWithType -> {
                builder.addStatement("$variableName.${property.name}")
            }

            is FieldWithFormList -> {
                //TODO
                builder.addStatement("emptyList()")
            }
        }
        return builder.build()
    }
}