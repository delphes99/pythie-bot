package fr.delphes.dynamicForm.descriptor

import fr.delphes.annotation.dynamicForm.DynamicFormFamily
import fr.delphes.dynamicForm.DynamicFormDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("FORM_LIST")
@Serializable
data class FormListFieldDescriptor(
    override val fieldName: String,
    override val description: String,
    val formFamily: DynamicFormFamily,
    override val value: List<DynamicFormDescription>,
) : FieldDescriptor()