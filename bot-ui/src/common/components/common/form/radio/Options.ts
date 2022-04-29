import { Option } from "@/common/components/common/form/radio/Option"
import FeatureDescriptionType from "@/features/configurations/FeatureDescriptionType.js"

export class Options<T> {
  public readonly values: Option<T>[]

  constructor(values: Option<T>[]) {
    this.values = values
  }

  static forStrings(values: string[]): Options<string> {
    return new this(values.map((value) => new Option<string>(value, (val) => val)))
  }

  static for<T>(values: T[], displayFunction: (value:T) => string): Options<T> {
    return new this(values.map((value) => new Option(value, displayFunction)))
  }
}