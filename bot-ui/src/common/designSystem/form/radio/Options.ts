import {Option} from "@/common/designSystem/form/radio/Option"

export class Options<T> {
    public readonly values: Option<T>[]

    constructor(values: Option<T>[]) {
        this.values = values
    }

    static for<T extends Object>(
        values: T[],
        displayFunction: (value: T) => string = (obj => obj.toString()),
        buildId: (value: T) => string = () => crypto.randomUUID(),
    ): Options<T> {
        return new Options(values.map((value) => new Option(value, displayFunction, buildId(value))))
    }
}