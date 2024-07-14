package fr.delphes.generation.dynamicForm.generateFormProcessor

import fr.delphes.annotation.dynamicForm.DynamicForm
import fr.delphes.annotation.dynamicForm.DynamicFormParent
import fr.delphes.annotation.dynamicForm.FieldDescription

@DynamicFormParent("form-family")
interface SubForms

@DynamicForm("string-form")
class StringSubForm(
    @FieldDescription("description")
    val value: String = "",
) : SubForms