import { NamedValue, Path, TranslateOptions } from "vue-i18n"

export type TranslateFunction = {
  (key: Path | number): string
  (key: Path | number, plural: number, options?: TranslateOptions): string
  (key: Path | number, defaultMsg: string, options?: TranslateOptions): string
  (key: Path | number, list: unknown[], options?: TranslateOptions): string
  (key: Path | number, list: unknown[], plural: number): string
  (key: Path | number, list: unknown[], defaultMsg: string): string
  (key: Path | number, named: NamedValue, options?: TranslateOptions): string
  (key: Path | number, named: NamedValue, plural: number): string
  (key: Path | number, named: NamedValue, defaultMsg: string): string
}