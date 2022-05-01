import { Option } from "@/common/components/common/form/radio/Option"

export class Options<T> {
  public readonly values: Option<T>[]

  constructor(values: Option<T>[]) {
    this.values = values
  }

  static for<T>(values: T[], displayFunction: (value: T) => string): Options<T> {
    return new this(values.map((value) => new Option(value, displayFunction)))
  }
}