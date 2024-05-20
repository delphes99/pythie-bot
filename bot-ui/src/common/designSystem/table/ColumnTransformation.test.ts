import {ColumnTransformation} from "@/common/designSystem/table/ColumnTransformation";
import {describe, expect, it} from "vitest";

describe("ColumnTransformation", () => {
    it("transform serialized date format to user friendly display", () => {
        expect(ColumnTransformation.DATE("2023-06-04T18:38:05.5224516")).toStrictEqual("04/06/2023 18:38:05");
    })
});