package fr.delphes.generation.dynamicForm.metadata.type.list

import fr.delphes.annotation.dynamicForm.DynamicForm
import fr.delphes.annotation.dynamicForm.DynamicFormParent

@DynamicFormParent("innerFormFamily")
interface ListInnerFormSource

@DynamicForm("innerForm")
class ListDynamicFormSourceImpl(
    val value: String,
)