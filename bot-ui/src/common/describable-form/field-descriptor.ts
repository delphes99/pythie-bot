export class FieldValue<T> {
    constructor(
        readonly fieldName: String,
        readonly value: T
    ) {}
}

export interface FieldDescriptor<T> {
    readonly fieldName: String
    readonly description: String

    buildValue(): FieldValue<T>

    viewComponent(): any
}