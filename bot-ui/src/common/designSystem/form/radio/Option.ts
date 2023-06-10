import {v4 as uuid} from "uuid"

export class Option<T> {
    public readonly id: string
    public readonly value: T
    private readonly display: (data: T) => string

    constructor(value: T, display: (data: T) => string, id: string = uuid()) {
        this.id = id
        this.value = value
        this.display = display
    }

    public label(): string {
        return this.display(this.value)
    }
}