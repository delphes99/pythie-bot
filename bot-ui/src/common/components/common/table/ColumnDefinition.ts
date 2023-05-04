export class ColumnDefinition<T> {
    public readonly name: string
    public readonly display: (data: T) => string

    constructor(name: string, display: (data: T) => string) {
        this.name = name
        this.display = display
    }
}