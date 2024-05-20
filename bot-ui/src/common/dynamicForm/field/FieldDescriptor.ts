export class FieldValue<T> {
    constructor(
        readonly fieldName: string,
        readonly value: T
    ) {
    }
}

export interface FieldDescriptor<T> {
    readonly fieldName: string
    readonly description: string

    buildValue(): FieldValue<T>

    viewComponent(): any
}