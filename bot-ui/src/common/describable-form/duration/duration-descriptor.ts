import {FieldDescriptorType} from "@/common/describable-form/field-descriptor-type";
import {FieldDescriptor, FieldValue} from "@/common/describable-form/field-descriptor";
import FieldDurationEditView from "@/common/describable-form/duration/field-duration-edit-view.vue";
import {DescriptorJsonType} from "@/features/descriptor-factory";
import {Duration, formatDuration, parseDuration} from "@/common/duration.utils";

export class DurationDescriptor implements FieldDescriptor<string> {
    actualValue: Duration
    readonly type: FieldDescriptorType = FieldDescriptorType.DURATION

    constructor(
        readonly description: string,
        readonly fieldName: string,
        readonly initialValue: Duration
    ) {
        this.actualValue = initialValue
    }

    static fromJson(json: DescriptorJsonType): DurationDescriptor {
        if (json.type !== FieldDescriptorType.DURATION) {
            throw new Error(`Cannot deserialize ${json.type} as DurationDescriptor`);
        }
        return new DurationDescriptor(
            json.description,
            json.fieldName,
            parseDuration(json.value)
        );
    }

    buildValue() {
        return new FieldValue(this.fieldName, formatDuration(this.actualValue));
    }

    viewComponent() {
        return FieldDurationEditView
    }
}