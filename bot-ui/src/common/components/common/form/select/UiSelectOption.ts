export class UiSelectOption<T> {
    option: T;
    toDisplay: (value: T) => string;

    constructor(option: T, toDisplay: (value: T) => string) {
        this.option = option
        this.toDisplay = toDisplay
    }

    static for<T>(values: T[] | null, displayFunction: (value: T) => string): UiSelectOption<T>[] {
        return (values || []).map((value) => new UiSelectOption(value, displayFunction))
    }

    static forString(values: string[] | null): UiSelectOption<string>[] {
        return (values || []).map((value) => new UiSelectOption(value, (value: string) => value))
    }

    display() {
        return this.toDisplay(this.option)
    }
}