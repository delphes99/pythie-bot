import TextComponentProperties from "@/overlay/editor/component/textComponent/TextComponentProperties.vue"
import TextComponentRender from "@/overlay/editor/component/textComponent/TextComponentRender.vue"
import {OverlayElementProperties, PropertiesComponent, RenderComponent} from "@/overlay/OverlayElementProperties"

export default class TextComponent implements OverlayElementProperties {
    type = "Text"
    text: string
    font: string
    fontSize: string
    color: string

    constructor(
        text: string,
        color: string,
        font: string,
        fontSize: string,
    ) {
        this.text = text
        this.color = color
        this.font = font
        this.fontSize = fontSize
    }

    public get representation() {
        return this.text
    }

    public static fromObject(obj: {
        text: string
        font: string
        fontSize: string
        color: string
    }): TextComponent {
        const {text, font, fontSize, color} = obj
        return new TextComponent(text, color, font, fontSize)
    }

    renderComponent(): RenderComponent {
        return TextComponentRender
    }

    propertiesComponent(): PropertiesComponent {
        return TextComponentProperties
    }

    equals(other: OverlayElementProperties): boolean {
        return (
            other instanceof TextComponent &&
            this.text === other.text &&
            this.color === other.color &&
            this.font === other.font &&
            this.fontSize === other.fontSize
        )
    }
}