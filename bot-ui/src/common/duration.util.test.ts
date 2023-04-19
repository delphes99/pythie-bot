import {describe, expect, it} from "vitest";
import {formatDuration, parseDuration} from "@/common/duration.utils";

describe("DurationUtil", () => {
    it("convert ISO8601 to Duration", () => {
        expect(parseDuration("PT1H20M30S")).toStrictEqual({
            hours: 1,
            minutes: 20,
            seconds: 30,
        });
    })
    it("convert Duration to ISO8601", () => {
        expect(formatDuration({
            hours: 1,
            minutes: 20,
            seconds: 30,
        })).toStrictEqual("PT1H20M30S");
    })
});