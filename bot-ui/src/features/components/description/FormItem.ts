export abstract class FormItem {
    public id: string
    public field: string

    protected constructor(id: string, field: string) {
        this.id = id
        this.field = field
    }

    abstract appendToResult(newFeature: any): void

    abstract isEmpty(): boolean

    abstract viewComponent(): any

    abstract editComponent(): any
}