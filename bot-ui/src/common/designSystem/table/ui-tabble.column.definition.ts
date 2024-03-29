import {Slot} from "vue";

export type TransformValue = (item: unknown) => unknown

export class UiTabbleColumnDefinition {
    constructor(
        readonly headerName: string,
        readonly render: Slot | undefined,
        readonly extractValue: TransformValue | undefined,
        readonly id: string = crypto.randomUUID(),
    ) {
    }
}