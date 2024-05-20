import {TestFieldDescriptor} from "@/common/dynamicForm/TestFieldDescriptor.test.utils";
import FeatureDescription from "@/features/FeatureDescription";
import {describe, expect, it} from "vitest";

describe("Feature description", () => {
    it("build value should return id and type in json format", () => {
        const featureDescription = new FeatureDescription(
            "type",
            "id",
            []
        );
        expect(featureDescription.buildValue()).toStrictEqual({
            id: "id",
            type: "type",
        });
    })
    it("build value should return field values in json format", () => {
        const featureDescription = new FeatureDescription(
            "type",
            "id",
            [
                new TestFieldDescriptor("fieldName", "first value"),
                new TestFieldDescriptor("anotherField", "second value"),
            ]
        );
        expect(featureDescription.buildValue()).toStrictEqual({
            id: "id",
            type: "type",
            fieldName: "first value",
            anotherField: "second value",
        });
    })
    it("modify descriptor", () => {
        const featureDescription = new FeatureDescription(
            "type",
            "id",
            [
                new TestFieldDescriptor("fieldName", "first value"),
                new TestFieldDescriptor("anotherField", "second value"),
                new TestFieldDescriptor("third", "third value"),
            ]
        );

        const newFeatureDescription = featureDescription.modifyDescriptor(new TestFieldDescriptor("anotherField", "new value"))

        expect(newFeatureDescription.buildValue()).toStrictEqual({
            id: "id",
            type: "type",
            fieldName: "first value",
            anotherField: "new value",
            third: "third value",
        });
    });
});

