import {Option} from "@/common/designSystem/form/radio/Option"
import {v4 as uuid} from "uuid";

export class Options<T> {
    public readonly values: Option<T>[]

    constructor(values: Option<T>[]) {
        this.values = values
    }

    static for<T extends Object>(
        values: T[],
        displayFunction: (value: T) => string = (obj => obj.toString()),
        buildId: (value: T) => string = () => uuid(),
    ): Options<T> {
        return new Options(values.map((value) => new Option(value, displayFunction, buildId(value))))
    }
}