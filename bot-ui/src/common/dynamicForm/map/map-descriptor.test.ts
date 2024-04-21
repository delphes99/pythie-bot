import {FieldValue} from "@/common/dynamicForm/field-descriptor";
import {FieldDescriptorType} from "@/common/dynamicForm/field-descriptor-type";
import {MapDescriptor} from "@/common/dynamicForm/map/map-descriptor";
import {describe, expect, it} from "vitest";

describe("Map descriptor", () => {
    it("construct from JSON", () => {
        const descriptor = MapDescriptor.fromJson({
            type: FieldDescriptorType.STRING,
            fieldName: "fieldName",
            description: "description",
            value: '{"key": "value", "key2": "value2"}',
        });

        expect(descriptor).toStrictEqual(
            new MapDescriptor(
                "description",
                "fieldName",
                '{"key": "value", "key2": "value2"}'
            )
        )
    })
    it("build with actual value", () => {
        const descriptor = new MapDescriptor("description", "fieldName", '{"key": "value", "key2": "value2"}');

        const value = descriptor.buildValue();

        expect(value).toStrictEqual(new FieldValue("fieldName", '{"key": "value", "key2": "value2"}'));
    })
    it("copy with new value", () => {
        const descriptor = new MapDescriptor("description", "fieldName", '{"key": "value", "key2": "value2"}');

        const newDescriptor = descriptor.withValue('{"key": "newValue", "key2": "value2"}').buildValue();

        expect(newDescriptor).toStrictEqual(new FieldValue("fieldName", '{"key": "newValue", "key2": "value2"}'));
    });
});