import { Option } from "@/common/components/common/form/radio/Option"

export class UiSelectOption<T> {
  option: T;
  toDisplay: (value: T) => string;

  constructor(option: T, toDisplay: (value: T) => string) {
    this.option = option
    this.toDisplay = toDisplay
  }

  display() {
    return this.toDisplay(this.option)
  }

  static for<T>(values: T[], displayFunction: (value: T) => string): UiSelectOption<T>[] {
    return values.map((value) => new UiSelectOption(value, displayFunction))
  }
}