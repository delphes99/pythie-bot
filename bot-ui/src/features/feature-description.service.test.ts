import {describe, expect, it} from "vitest";
import FeatureDescription, {FeatureConfiguration, FeatureDescriptor} from "@/features/feature-description";
import FeatureDescriptionService, {SetValue} from "@/features/feature-description.service";

const descriptionService = new FeatureDescriptionService()

describe("FeatureDescriptionService", () => {
    const descriptor = new FeatureDescription(
        "type",
        "some-id",
        [
            new FeatureDescriptor(
                "fieldName",
                "description",
                "STRING",
                "value for field 1"
            ),
            new FeatureDescriptor(
                "fieldName2",
                "description",
                "STRING",
                "value for field 2"
            ),
            new FeatureDescriptor(
                "otherFieldName",
                "description",
                "STRING",
                "value for other field"
            )
        ]
    )

    it("configuration without modification", () => {
        const expectedPayload = {
            id: "some-id",
            type: "type",
            fieldName: "value for field 1",
            fieldName2: "value for field 2",
            otherFieldName: "value for other field"
        }

        const payload = descriptionService.buildConfiguration(descriptor);

        expect(payload).toEqual(expectedPayload)
    })

    it("configuration with modification", () => {
        const expectedPayload: FeatureConfiguration = {
            id: "some-id",
            type: "type",
            fieldName: "new value",
            fieldName2: "value for field 2",
            otherFieldName: "new value for other field"
        }

        const payload = descriptionService.buildConfiguration(
            descriptor,
            new SetValue("fieldName", "new value"),
            new SetValue("otherFieldName", "new value for other field")
        )

        expect(payload).toEqual(expectedPayload)
    })
})
