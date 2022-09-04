package fr.delphes.descriptor

import kotlin.reflect.KClass

@kotlinx.serialization.Serializable
@JvmInline
value class DescriptorType(val value: String) {
    constructor(clazz: KClass<*>): this(clazz.qualifiedName!!.replace('.', '-'))
}

fun <U : KClass<*>> U.toDescriptorType(): DescriptorType = DescriptorType(this)