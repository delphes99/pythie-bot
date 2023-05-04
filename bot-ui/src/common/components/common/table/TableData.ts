import {ColumnDefinition} from "@/common/components/common/table/ColumnDefinition.js"

export class TableData<T> {
    public readonly data: T[]
    public readonly columns: ColumnDefinition<T>[]

    constructor(data: T[], columns: ColumnDefinition<T>[]) {
        this.data = data
        this.columns = columns
    }
}