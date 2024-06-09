import {Field} from "@/common/dynamicForm/field/Field";
import {FieldJsonType} from "@/common/dynamicForm/field/Field.factory";
import {FieldJsonValue} from "@/common/dynamicForm/field/FieldJsonValue";
import {Duration, formatDuration, parseDuration} from "@/common/utils/Duration";
import {Component} from "vue";
import FieldDurationEditView from "./FieldDurationEditView.vue";


export class FieldDuration implements Field<Duration> {
    static fromJson(json: FieldJsonType) {
        return new FieldDuration(
            json.description,
            json.fieldName,
            parseDuration(json.value)
        )
    }

    readonly id: string = crypto.randomUUID()

    constructor(
        public readonly description: string,
        public readonly fieldName: string,
        public readonly initialValue: Duration,
        public readonly actualValue: Duration = initialValue
    ) {
    }

    viewComponent(): Component {
        return FieldDurationEditView
    }

    buildJsonValue(): FieldJsonValue {
        return new FieldJsonValue(this.fieldName, formatDuration(this.actualValue));
    }
}