package fr.delphes.generation.dynamicForm.generateFormProcessor

import fr.delphes.annotation.dynamicForm.DynamicForm
import fr.delphes.annotation.dynamicForm.DynamicFormParent

@DynamicFormParent("form-family")
interface SubForms

@DynamicForm("string-form")
class StringSubForm(
    val value: String = "",
) : SubForms