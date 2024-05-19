import {Field} from "@/common/dynamicForm/field/field";
import {FieldJsonType} from "@/common/dynamicForm/field/field.factory";
import {Duration, parseDuration} from "@/common/utils/duration.utils";
import {Component} from "vue";
import FieldDurationEditView from "./field-duration-edit-view.vue";


export class FieldDuration implements Field<Duration> {
    constructor(
        public readonly description: string,
        public readonly fieldName: string,
        public readonly initialValue: Duration,
        public readonly actualValue: Duration = initialValue
    ) {
    }

    viewComponent(): Component {
        return FieldDurationEditView;
    }

    static fromJson(json: FieldJsonType) {
        return new FieldDuration(
            json.description,
            json.fieldName,
            parseDuration(json.value)
        );
    }
}