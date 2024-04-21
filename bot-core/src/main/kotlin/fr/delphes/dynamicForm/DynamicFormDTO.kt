package fr.delphes.dynamicForm

interface DynamicFormDTO<T> {
    //    val type: DynamicFormType
    fun description(): DynamicFormDescription
//    fun build(): T
}