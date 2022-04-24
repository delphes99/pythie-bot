import { ColumnDefinition } from "@/common/components/common/table/ColumnDefinition.js"

export class TableData<T> {
  public readonly data: T[]
  public readonly columns: ColumnDefinition<T>[]
  public readonly emptyMessage?: string

  constructor(data: T[], columns: ColumnDefinition<T>[], emptyMessage?: string) {
    this.data = data
    this.columns = columns
    this.emptyMessage = emptyMessage
  }
}