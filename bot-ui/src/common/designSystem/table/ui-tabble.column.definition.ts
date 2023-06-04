import {v4 as uuid} from "uuid";
import {Slot} from "vue";

export class UiTabbleColumnDefinition {
    constructor(
        readonly headerName: string,
        readonly render: Slot | undefined,
        readonly propertyName: string | undefined,
        readonly id: string = uuid(),
    ) {
    }
}