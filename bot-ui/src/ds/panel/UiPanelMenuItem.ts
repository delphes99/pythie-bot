import UiButtonType from "@/ds/button/UiButtonType"

export class UiPanelMenuItem {
  constructor(
    label: string,
    type: UiButtonType = UiButtonType.Primary,
    onClick: () => void = () => {
      /* Nothing */
    },
  ) {
    this.label = label
    this.type = type
    this.onClick = onClick
  }

  label: string
  type: UiButtonType
  onClick: () => void
}