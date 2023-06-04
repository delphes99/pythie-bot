import UiButtonType from "@/common/designSystem/button/UiButtonType"

export class UiPanelMenuItem {
    label: string
    type: UiButtonType
    onClick: () => void

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

    static of(label: string, onClick: () => void): UiPanelMenuItem {
        return new UiPanelMenuItem(label, UiButtonType.Primary, onClick)
    }
}